package com.team4.testingsystem.enums;

public enum QuestionStatus {
    ARCHIVED("Archived"),
    UNARCHIVED("Unarchived");

    private final String name;

    QuestionStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
