package com.team4.testingsystem.exceptions;

public class FileNotFoundException extends NotFoundException {
    public FileNotFoundException() {
        super("File not found");
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
