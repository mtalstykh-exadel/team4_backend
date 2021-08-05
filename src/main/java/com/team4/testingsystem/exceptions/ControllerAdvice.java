package com.team4.testingsystem.exceptions;

import com.team4.testingsystem.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConflictException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(value = {FileOperationException.class})
    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse handleFileOperationException(FileOperationException e) {
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler(value = {IncorrectCredentialsException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotFoundException(IncorrectCredentialsException e) {
        return new ErrorResponse("Incorrect login or password");
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }
}
