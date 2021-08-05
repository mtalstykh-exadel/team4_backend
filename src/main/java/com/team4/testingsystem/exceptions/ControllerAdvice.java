package com.team4.testingsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConflictException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Response handleConflictException(ConflictException e) {
        return new Response(e.getMessage());
    }

    @ExceptionHandler(value = {FileOperationException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleFileOperationException(FileOperationException e) {
        return new Response(e.getMessage());
    }


    @ExceptionHandler(value = {IncorrectCredentialsException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Response handleNotFoundException(IncorrectCredentialsException e) {
        return new Response("Incorrect login or password");
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Response handleNotFoundException(NotFoundException e) {
        return new Response(e.getMessage());
    }
}
