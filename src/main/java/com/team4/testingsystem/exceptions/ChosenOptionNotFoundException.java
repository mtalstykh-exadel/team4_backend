package com.team4.testingsystem.exceptions;

public class ChosenOptionNotFoundException extends NotFoundException {
    public ChosenOptionNotFoundException() {
        super("Chosen option not found");
    }
}
