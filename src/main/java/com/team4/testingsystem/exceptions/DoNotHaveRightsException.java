package com.team4.testingsystem.exceptions;

public class DoNotHaveRightsException extends RuntimeException {
    public DoNotHaveRightsException(String message) {
        super(message);
    }
}

