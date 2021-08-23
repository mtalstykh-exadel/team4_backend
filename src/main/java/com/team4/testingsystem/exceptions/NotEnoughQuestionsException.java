package com.team4.testingsystem.exceptions;

public class NotEnoughQuestionsException extends NotFoundException {
    public NotEnoughQuestionsException(String message) {
        super(message);
    }
}
