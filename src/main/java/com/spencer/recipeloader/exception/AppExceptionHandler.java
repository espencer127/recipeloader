package com.spencer.recipeloader.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RestClientException.class})
    @Override
    protected ResponseEntity<Object> createResponseEntity(Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (!(body instanceof ProblemDetail)) {
            return super.createResponseEntity(body, headers, statusCode, request);
        }

        // Add custom details
        ProblemDetail p = (ProblemDetail) body;
        p.setProperty("env", "dev");

        return super.createResponseEntity(body, headers, statusCode, request);
    }

    
}