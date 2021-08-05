package com.team4.testingsystem.exceptions;

public class LevelNotFoundException extends NotFoundException {
    public LevelNotFoundException() {
        super("Level not found");
    }
}
