package com.team4.testingsystem.exceptions;

public class ModuleNotFoundException extends NotFoundException {

    public ModuleNotFoundException() {
        super("Module not found");
    }
}
