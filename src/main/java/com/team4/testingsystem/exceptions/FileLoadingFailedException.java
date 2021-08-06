package com.team4.testingsystem.exceptions;

public class FileLoadingFailedException extends FileOperationException {
    public FileLoadingFailedException() {
        super("File loading failed");
    }
}
