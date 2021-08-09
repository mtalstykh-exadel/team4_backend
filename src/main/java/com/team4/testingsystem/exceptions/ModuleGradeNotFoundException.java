package com.team4.testingsystem.exceptions;

public class ModuleGradeNotFoundException extends NotFoundException {
    public ModuleGradeNotFoundException(String moduleName) {
        super("Module (" + moduleName + ") grade not found");
    }
}
