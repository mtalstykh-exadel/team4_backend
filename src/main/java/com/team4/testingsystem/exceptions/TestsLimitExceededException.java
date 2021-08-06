package com.team4.testingsystem.exceptions;

public class TestsLimitExceededException extends RuntimeException {
    public TestsLimitExceededException(String comeBackDate) {
        super(comeBackDate);
    }
}
