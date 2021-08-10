package com.team4.testingsystem.exceptions;

public class ContentFileNotFoundException extends NotFoundException {
    public ContentFileNotFoundException() {
        super("Content file not found");
    }
}
