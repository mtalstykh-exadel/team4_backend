package com.team4.testingsystem.exceptions;

public class TestNotFoundException extends NotFoundException {

    public TestNotFoundException() {
        super("Test not found");
    }
}
