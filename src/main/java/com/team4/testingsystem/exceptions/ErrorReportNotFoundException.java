package com.team4.testingsystem.exceptions;

public class ErrorReportNotFoundException extends NotFoundException {
    public ErrorReportNotFoundException() {
        super("Error report not found");
    }
}

