package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.Test;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TestDTO implements Serializable {
    private Long id;
    private String level;
    private Instant assignedAt;
    private Instant completedAt;
    private Instant verifiedAt;
    private Instant startedAt;
    private Instant deadline;
    private String priority;
    private String status;
    private UserDTO coach;
    private Map<String, List<QuestionDTO>> questions;
    private ContentFileDTO contentFile;

    public TestDTO() {
    }

    public TestDTO(Test test) {
        id = test.getId();
        level = test.getLevel().getName();
        assignedAt = test.getAssignedAt();
        completedAt = test.getCompletedAt();
        verifiedAt = test.getVerifiedAt();
        startedAt = test.getStartedAt();
        deadline = test.getDeadline();
        priority = test.getPriority().getName();
        status = test.getStatus().name();
        if (test.getCoach() != null) {
            coach = new UserDTO(test.getCoach());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, List<QuestionDTO>> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, List<QuestionDTO>> questions) {
        this.questions = questions;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }

    public ContentFileDTO getContentFile() {
        return contentFile;
    }

    public void setContentFile(ContentFileDTO contentFile) {
        this.contentFile = contentFile;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
                && Objects.equals(assignedAt, testDTO.assignedAt)
                && Objects.equals(completedAt, testDTO.completedAt)
                && Objects.equals(verifiedAt, testDTO.verifiedAt)
                && Objects.equals(startedAt, testDTO.startedAt)
                && Objects.equals(deadline, testDTO.deadline)
                && Objects.equals(priority, testDTO.priority)
                && Objects.equals(status, testDTO.status)
                && Objects.equals(coach, testDTO.coach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, assignedAt, completedAt, verifiedAt, startedAt, deadline, priority, status, coach);
    }
}
