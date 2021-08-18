package com.team4.testingsystem.exceptions;

public class TooLongEssayException extends ConflictException {
    public TooLongEssayException() {
        super("The essay is too long");
    }
}
