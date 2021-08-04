package com.team4.testingsystem.exceptions;

import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {PSQLException.class})
    protected ResponseEntity<Object> handlePostgreSQLException(PSQLException e, WebRequest request) {
        String body = "Incorrect data provided!";

        final ServerErrorMessage errorMessage = e.getServerErrorMessage();
        if (errorMessage != null) {
            body = "Incorrect data provided for column '" + errorMessage.getColumn() + "'";
        }
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
