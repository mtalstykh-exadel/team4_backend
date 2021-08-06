package com.team4.testingsystem.exceptions;

public class GradeNotFoundException extends NotFoundException {
    public GradeNotFoundException() {
        super("Coach's grade not found");
    }
}
