package com.team4.testingsystem.exceptions;

public class FileDeletingFailedException extends FileOperationException {
    public FileDeletingFailedException() {
        super("File deleting failed");
    }
}
