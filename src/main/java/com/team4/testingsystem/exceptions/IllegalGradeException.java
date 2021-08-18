package com.team4.testingsystem.exceptions;

public class IllegalGradeException extends RuntimeException{
    public IllegalGradeException() {
        super("Grade must be from 0 to 10");
    }
}
