package com.team4.testingsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ChosenOptionBadRequestException extends RuntimeException {
    public ChosenOptionBadRequestException() {
        super("Chosen option parameters not exist");
    }
}
