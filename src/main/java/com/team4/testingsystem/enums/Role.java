package com.team4.testingsystem.enums;

public enum Role {
    USER("ROLE_USER"),
    HR("ROLE_HR"),
    COACH("ROLE_COACH"),
    ADMIN("ROLE_ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
