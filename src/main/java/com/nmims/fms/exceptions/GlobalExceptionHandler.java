package com.nmims.fms.exceptions;

import java.nio.file.AccessDeniedException;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.ServletException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Exception handler for generic exceptions
    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(
            Exception exception,
            WebRequest request) {

        System.out.println("Global Exception Handler called" + exception);
        String acceptHeader = request.getHeader("Accept");

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(new Date())
                .message(exception.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        if (acceptHeader != null && acceptHeader.contains("text/html")) {
            System.out.println("HTML response requested");
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("errorDetails", errorDetails);
            return mav;
        } else {
            System.out.println("JSON response requested");
            return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Exception Handler for Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(
            MethodArgumentNotValidException exception) {

        String errorMessage = exception.getBindingResult().getFieldError().getDefaultMessage();

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(new Date())
                .message(errorMessage)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Exception Handler for Database-related errors
    @ExceptionHandler(DbExceptionHandler.class)
    public ResponseEntity<ErrorDetails> handleDbException(
            DbExceptionHandler exception) {

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(new Date())
                .message(exception.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Exception Handler for Access Denied errors
    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDeniedException(
            AccessDeniedException exception,
            WebRequest request) {

        String acceptHeader = request.getHeader("Accept");

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(new Date())
                .message(exception.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .build();

        if (acceptHeader != null && acceptHeader.contains("text/html")) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("errorDetails", errorDetails);
            return mav;
        } else {
            return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
        }
    }

    // Exception Handler for Unauthorized Access
    @ExceptionHandler(UnauthorizedAccessException.class)
    public Object handleUnauthorizedAccess(UnauthorizedAccessException exception, WebRequest request) {

        String acceptHeader = request.getHeader("Accept");

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(new Date())
                .message(exception.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();

        if (acceptHeader != null && acceptHeader.contains("text/html")) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("errorDetails", errorDetails);
            return mav;
        } else {
            return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
        }
    }

    // Exception Handler for Forbidden Access
    @ExceptionHandler(ForbiddenAccessException.class)
    public Object handleForbiddenAccess(ForbiddenAccessException exception, WebRequest request) {

        String acceptHeader = request.getHeader("Accept");

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(new Date())
                .message(exception.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .build();

        if (acceptHeader != null && acceptHeader.contains("text/html")) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("errorDetails", errorDetails);
            return mav;
        } else {
            return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
        }
    }

    // Exception Handler for Invalid Request
    @ExceptionHandler(InvalidRequestException.class)
    public Object handleInvalidRequest(InvalidRequestException exception, WebRequest request) {
        String acceptHeader = request.getHeader("Accept");

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(new Date())
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        if (acceptHeader != null && acceptHeader.contains("text/html")) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("errorDetails", errorDetails);
            return mav;
        } else {
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }
    }

    // Exception Handler for Not Found
    @ExceptionHandler(NotFoundException.class)
    public Object handleNotFound(NotFoundException exception, WebRequest request) {
        String acceptHeader = request.getHeader("Accept");

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(new Date())
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        if (acceptHeader != null && acceptHeader.contains("text/html")) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("errorDetails", errorDetails);
            return mav;
        } else {
            return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }
    }

    // Exception Handler for JSP or Servlet-related errors
    @ExceptionHandler(ServletException.class)
    public Object handleServletException(ServletException exception, WebRequest request) {
        String acceptHeader = request.getHeader("Accept");

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(new Date())
                .message("An internal error occurred while rendering the view: " + exception.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        if (acceptHeader != null && acceptHeader.contains("text/html")) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("errorDetails", errorDetails);
            return mav;
        } else {
            return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
