package com.nmims.fms.exceptions;

import org.springframework.http.HttpStatus;

public class DbExceptionHandler extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public DbExceptionHandler(HttpStatus status, String message) {
        super(message); 
        this.status = status; 
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
