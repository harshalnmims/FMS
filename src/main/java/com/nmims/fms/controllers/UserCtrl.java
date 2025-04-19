package com.nmims.fms.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nmims.fms.exceptions.UnauthorizedAccessException;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserCtrl {

    @GetMapping("/user")
    public String getUser() {
        log.info("User Controller called");
        throw new UnauthorizedAccessException("User not found"); // Simulating an exception
    }
   
}
