package com.team4.testingsystem.exceptions;

public class AnswerNotFoundException extends NotFoundException {
    public AnswerNotFoundException() {
        super("Answer not found");
    }
}
