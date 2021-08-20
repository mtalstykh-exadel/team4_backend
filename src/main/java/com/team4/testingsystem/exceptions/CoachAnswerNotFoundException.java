package com.team4.testingsystem.exceptions;

public class CoachAnswerNotFoundException extends NotFoundException {
    public CoachAnswerNotFoundException() {
        super("Coach's answer not found");
    }
}
