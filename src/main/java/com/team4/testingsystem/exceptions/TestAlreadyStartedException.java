package com.team4.testingsystem.exceptions;

public class TestAlreadyStartedException extends ConflictException {
    public TestAlreadyStartedException(String message) {
        super(message);
    }
}
