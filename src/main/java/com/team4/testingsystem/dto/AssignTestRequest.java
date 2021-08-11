package com.team4.testingsystem.dto;

import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Priority;

import java.time.LocalDateTime;

public class AssignTestRequest {
    private Levels level;
    private LocalDateTime deadline;
    private Priority priority;

    public AssignTestRequest() {
    }

    public AssignTestRequest(Levels level, LocalDateTime deadline, Priority priority) {
        this.level = level;
        this.deadline = deadline;
        this.priority = priority;
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
