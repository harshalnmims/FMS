package com.nmims.fms.utils.pagination;

import java.util.List;

import lombok.Data;

@Data
public class PaginationInput {

    private int page;
    private int size;
    private List<FilterCriteria> filters; // Filters for WHERE clause
    private List<SearchCriteria> search;  // Search criteria for search functionality
    private List<OrderCriteria> order;   // Order criteria for ORDER BY clause
    private List<GroupCriteria> groupBy; // Group By Criteria
    
}
