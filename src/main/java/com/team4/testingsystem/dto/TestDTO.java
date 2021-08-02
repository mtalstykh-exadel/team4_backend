package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.Test;

import java.time.LocalDateTime;
import java.util.Objects;

public class TestDTO {
    private String level;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private String coach;

    public TestDTO(Test test){
        level = test.getLevel().getName();
        createdAt = test.getCreatedAt();
        finishedAt = test.getFinishedAt();
        coach = test.getCoach().getName();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestDTO testDTO = (TestDTO) o;
        return Objects.equals(level, testDTO.level)
               && Objects.equals(createdAt, testDTO.createdAt)
               && Objects.equals(finishedAt, testDTO.finishedAt)
               && Objects.equals(coach, testDTO.coach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, createdAt, finishedAt, coach);
    }
}
