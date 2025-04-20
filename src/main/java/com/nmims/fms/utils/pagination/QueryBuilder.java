package com.nmims.fms.utils.pagination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueryBuilder<T> {

    private final EntityManager entityManager;
    private final Class<T> entityClass;

    public QueryBuilder(EntityManager entityManager, Class<T> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    public Page<T> buildQuery(PaginationInput paginationInput) {

        // Create Pageable object
        Pageable pageable = PageRequest.of(paginationInput.getPage(), paginationInput.getSize());

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        Map<String, Join<?, ?>> joins = new HashMap<>();

        List<Predicate> predicates = new ArrayList<>();

        // Filters
        if (paginationInput.getFilters() != null && !paginationInput.getFilters().isEmpty()) {
            for (FilterCriteria filter : paginationInput.getFilters()) {
                predicates.add(buildFilterPredicate(cb, root, joins, filter));
            }
        }

        // Search
        if (paginationInput.getSearch() != null && !paginationInput.getSearch().isEmpty()) {
            List<Predicate> searchPredicates = new ArrayList<>();
            for (SearchCriteria search : paginationInput.getSearch()) {
                searchPredicates.add(buildSearchPredicate(cb, root, joins, search));
            }
            if (!searchPredicates.isEmpty()) {
                predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
            }
        }

        // Apply WHERE
        query.where(predicates.toArray(new Predicate[0]));

        // After WHERE, Group By
        if (paginationInput.getGroupBy() != null && !paginationInput.getGroupBy().isEmpty()) {
            List<Expression<?>> groupExpressions = new ArrayList<>();
            for (GroupCriteria groupCol : paginationInput.getGroupBy()) {
                groupExpressions.add(getPath(root, joins, groupCol.getTable(), groupCol.getColumn()));
            }
            query.groupBy(groupExpressions);
        }

        // Order By
        if (paginationInput.getOrder() != null && !paginationInput.getOrder().isEmpty()) {
            List<Order> orders = new ArrayList<>();
            for (OrderCriteria order : paginationInput.getOrder()) {
                orders.add(buildOrder(cb, root, joins, order));
            }
            query.orderBy(orders);
        }

        // Execute paged result
        List<T> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(entityClass);
        Map<String, Join<?, ?>> countJoins = new HashMap<>();
        List<Predicate> countPredicates = new ArrayList<>();

        if (paginationInput.getFilters() != null) {
            for (FilterCriteria filter : paginationInput.getFilters()) {
                countPredicates.add(buildFilterPredicate(cb, countRoot, countJoins, filter));
            }
        }
        if (paginationInput.getSearch() != null) {
            List<Predicate> searchPredicates = new ArrayList<>();
            for (SearchCriteria search : paginationInput.getSearch()) {
                searchPredicates.add(buildSearchPredicate(cb, countRoot, countJoins, search));
            }
            if (!searchPredicates.isEmpty()) {
                countPredicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
            }
        }

        countQuery.select(cb.count(countRoot));
        countQuery.where(countPredicates.toArray(new Predicate[0]));
        Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, totalRecords);
    }

    private Predicate buildFilterPredicate(CriteriaBuilder cb, Root<T> root, Map<String, Join<?, ?>> joins,
            FilterCriteria filter) {
        Path<?> path = getPath(root, joins, filter.getTable(), filter.getColumn());

        switch (filter.getOperator().toLowerCase()) {
            case "=":
                return cb.equal(path, filter.getValue());
            case "!=":
                return cb.notEqual(path, filter.getValue());
            case ">":
                return cb.greaterThan(path.as(String.class), filter.getValue().toString());
            case "<":
                return cb.lessThan(path.as(String.class), filter.getValue().toString());
            case ">=":
                return cb.greaterThanOrEqualTo(path.as(String.class), filter.getValue().toString());
            case "<=":
                return cb.lessThanOrEqualTo(path.as(String.class), filter.getValue().toString());
            case "like":
                return cb.like(path.as(String.class), "%" + filter.getValue() + "%");
            case "ilike":
                return cb.like(cb.lower(path.as(String.class)), "%" + filter.getValue().toString().toLowerCase() + "%");
            default:
                throw new IllegalArgumentException("Unsupported operator: " + filter.getOperator());
        }
    }

    private Predicate buildSearchPredicate(CriteriaBuilder cb, Root<T> root, Map<String, Join<?, ?>> joins,
            SearchCriteria search) {
        Path<?> path = getPath(root, joins, search.getTable(), search.getColumn());
        return cb.like(cb.lower(path.as(String.class)), "%" + search.getValue().toString().toLowerCase() + "%");
    }

    private Order buildOrder(CriteriaBuilder cb, Root<T> root, Map<String, Join<?, ?>> joins, OrderCriteria order) {
        Path<?> path = getPath(root, joins, order.getTable(), order.getColumn());
        return "desc".equalsIgnoreCase(order.getOrderType()) ? cb.desc(path) : cb.asc(path);
    }

    private Path<?> getPath(Root<T> root, Map<String, Join<?, ?>> joins, String table, String column) {
        if (table == null || table.trim().isEmpty()
                || table.equalsIgnoreCase(root.getJavaType().getSimpleName().toLowerCase())) {
            log.warn("inside same table check");
            return root.get(column);
        }

        log.info("table join " + joins + " :: " + table);

        if (!joins.containsKey(table)) {
            try {
                joins.put(table, root.join(table));
            } catch (IllegalArgumentException ex) {
                log.warn("Skipping invalid join on table: {}", table);
                return root.get(column); // fallback to root
            }
        }

        return joins.get(table).get(column);
    }
}
