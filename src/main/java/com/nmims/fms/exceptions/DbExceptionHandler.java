package com.nmims.fms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DbExceptionHandler extends RuntimeException {
   
    public DbExceptionHandler(String message) {
        super(message);
    }
}
