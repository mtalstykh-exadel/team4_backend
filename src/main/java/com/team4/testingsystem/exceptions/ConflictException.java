package com.team4.testingsystem.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException() {
        super("Conflict");
    }

    public ConflictException(String message) {
        super(message);
    }
}
