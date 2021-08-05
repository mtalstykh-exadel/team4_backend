package com.team4.testingsystem.exceptions;

public class FileSavingFailedException extends FileOperationException {

    public FileSavingFailedException() {
        super("File saving failed");
    }
}
