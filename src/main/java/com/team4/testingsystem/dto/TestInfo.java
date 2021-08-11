package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.Test;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class TestInfo implements Serializable {
    private String level;
    private LocalDateTime deadline;
    private String priority;

    public TestInfo() {
    }

    public TestInfo(Test test) {
        this.level = test.getLevel().getName();
        this.deadline = test.getDeadline();
        this.priority = test.getPriority().getName();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestInfo testInfo = (TestInfo) o;
        return Objects.equals(level, testInfo.level)
                && Objects.equals(deadline, testInfo.deadline)
                && Objects.equals(priority, testInfo.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, deadline, priority);
    }
}
