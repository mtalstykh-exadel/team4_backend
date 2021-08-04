package com.team4.testingsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ConflictExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConflictException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Response handleConflictException(ConflictException e) {
        return new Response(e.getMessage());
    }
}
