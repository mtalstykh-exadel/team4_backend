package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.Test;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class TestInfo implements Serializable {
    private String level;
    private LocalDateTime deadline;

    public TestInfo() {
    }

    public TestInfo(Test test) {
        this.level = test.getLevel().getName();
        this.deadline = test.getDeadline();
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
                && Objects.equals(deadline, testInfo.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, deadline);
    }
}
