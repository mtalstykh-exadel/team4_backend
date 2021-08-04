package com.team4.testingsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class IncorrectCredentialsExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IncorrectCredentialsException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Response handleNotFoundException(IncorrectCredentialsException e) {
        return new Response("Incorrect login or password");
    }

}
