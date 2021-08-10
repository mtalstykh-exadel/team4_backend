package com.team4.testingsystem.exceptions;

public class CoachGradeNotFoundException extends NotFoundException {
    public CoachGradeNotFoundException() {
        super("Coach's grade not found");
    }
}
