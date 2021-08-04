package com.team4.testingsystem.exceptions;

public class CoachGradeAlreadyExistsException extends ConflictException {
    public CoachGradeAlreadyExistsException() {
        super("Grade already exists");
    }
}
