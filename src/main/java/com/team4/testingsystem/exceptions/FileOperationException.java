package com.team4.testingsystem.exceptions;

public class FileOperationException extends RuntimeException {
    public FileOperationException() {
        super("File fail");
    }

    public FileOperationException(String message) {
        super(message);
    }
}
