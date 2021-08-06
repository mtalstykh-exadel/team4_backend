package com.team4.testingsystem.dto;

import com.team4.testingsystem.enums.Levels;

import java.time.LocalDateTime;

public class AssignTestRequest {
    private Levels level;
    private LocalDateTime deadline;

    public AssignTestRequest() {
    }

    public AssignTestRequest(Levels level, LocalDateTime deadline) {
        this.level = level;
        this.deadline = deadline;
    }

    public Levels getLevel() {
        return level;
    }

    public void setLevel(Levels level) {
        this.level = level;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
