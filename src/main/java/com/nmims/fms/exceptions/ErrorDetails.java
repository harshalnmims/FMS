package com.nmims.fms.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private HttpStatus status;

    public ErrorDetails(Date timestamp, String message, HttpStatus status) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
    }
}