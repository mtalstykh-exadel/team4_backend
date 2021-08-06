package com.team4.testingsystem.exceptions;

public class CoachAssignmentFailException extends ConflictException {
    public CoachAssignmentFailException() {
        super("Coach can't verify his own test");
    }
}
