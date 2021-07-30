package com.team4.testingsystem.enums;

public enum Modules {
    LISTENING("Listening"),
    ESSAY("Essay"),
    GRAMMAR("Grammar"),
    SPEAKING("Speaking");

    private final String name;

    Modules(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
