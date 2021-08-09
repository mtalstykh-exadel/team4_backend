package com.team4.testingsystem.exceptions;

public class QuestionNotFoundException extends NotFoundException {
    public QuestionNotFoundException() {
        super("Question not found");
    }
}
