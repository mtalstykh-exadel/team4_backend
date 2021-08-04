package com.team4.testingsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team4.testingsystem.entities.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestDTO {
    private String level;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime startedAt;
    private UserDTO coach;
    private int evaluation;
    private Map<String, List<QuestionDTO>> questions;
    private String contentFile;

    public TestDTO() {
    }

    public TestDTO(Test test) {
        level = test.getLevel().getName();
        createdAt = test.getCreatedAt();
        finishedAt = test.getFinishedAt();
        updatedAt = test.getUpdatedAt();
        startedAt = test.getStartedAt();
        evaluation = test.getEvaluation();
        if (test.getCoach() != null) {
            coach = new UserDTO(test.getCoach());
        }
    }

    public UserDTO getCoach() {
        return coach;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public Map<String, List<QuestionDTO>> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, List<QuestionDTO>> questions) {
        this.questions = questions;
    }

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    public void setCoach(UserDTO coach) {
        this.coach = coach;
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

    public UserDTO getCoach() {
        return coach;
    }

    public void setCoach(UserDTO coach) {
        this.coach = coach;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestDTO testDTO = (TestDTO) o;
        return Objects.equals(level, testDTO.level)
               && Objects.equals(createdAt, testDTO.createdAt)
               && Objects.equals(finishedAt, testDTO.finishedAt)
               && Objects.equals(updatedAt, testDTO.updatedAt)
               && Objects.equals(startedAt, testDTO.startedAt)
               && Objects.equals(coach, testDTO.coach);
               && Objects.equals(coach, testDTO.coach)
               && Objects.equals(questions, testDTO.questions)
               && Objects.equals(contentFile, testDTO.contentFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, createdAt, finishedAt, updatedAt, startedAt, coach);
        return Objects.hash(level,
                createdAt,
                finishedAt,
                coach,
                questions,
                contentFile);
    }
}
