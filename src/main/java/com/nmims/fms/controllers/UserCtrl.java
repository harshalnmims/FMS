package com.nmims.fms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nmims.fms.dto.User;
import com.nmims.fms.exceptions.UnauthorizedAccessException;
import com.nmims.fms.utils.pagination.PaginationInput;
import com.nmims.fms.utils.pagination.QueryBuilder;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserCtrl {

    @Autowired
    private EntityManager entityManager;

    @PostMapping("/paginate")
    public ResponseEntity<Page<User>> getPaginatedUsers(@RequestBody PaginationInput paginationInput) {

        QueryBuilder<User> queryBuilder = new QueryBuilder<>(entityManager, User.class);
        // Build and execute the query
        Page<User> paginatedUsers = queryBuilder.buildQuery(paginationInput);

        return ResponseEntity.ok(paginatedUsers);
    }
   
}
