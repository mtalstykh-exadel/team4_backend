package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.Test;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class TestInfo implements Serializable {
    private Long testId;
    private String level;
    private Instant assigned;
    private Instant deadline;
    private Instant verified;
    private String status;
    private String priority;
    private Instant completedAt;
    private Instant startedAt;
    private UserDTO coach;

    public TestInfo() {
    }

    public TestInfo(Test test) {
        testId = test.getId();
        level = test.getLevel().getName();
        deadline = test.getDeadline();
        priority = test.getPriority().getName();
        assigned = test.getAssignedAt();
        verified = test.getVerifiedAt();
        status = test.getStatus().name();
        completedAt = test.getCompletedAt();
        startedAt = test.getStartedAt();
        if (test.getCoach() != null) {
            coach = new UserDTO(test.getCoach());
        }
    }

    public UserDTO getCoach() {
        return coach;
    }

    public void setCoach(UserDTO coach) {
        this.coach = coach;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getAssigned() {
        return assigned;
    }

    public void setAssigned(Instant assigned) {
        this.assigned = assigned;
    }

    public Instant getVerified() {
        return verified;
    }

    public void setVerified(Instant verified) {
        this.verified = verified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public void setDeadline(Instant deadline) {
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
        return Objects.equals(testId, testInfo.testId)
               && Objects.equals(level, testInfo.level)
               && Objects.equals(assigned, testInfo.assigned)
               && Objects.equals(deadline, testInfo.deadline)
               && Objects.equals(verified, testInfo.verified)
               && Objects.equals(status, testInfo.status)
               && Objects.equals(priority, testInfo.priority)
               && Objects.equals(completedAt, testInfo.completedAt)
               && Objects.equals(startedAt, testInfo.startedAt)
               && Objects.equals(coach, testInfo.coach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId,
                level,
                assigned,
                deadline,
                verified,
                status,
                priority,
                completedAt,
                startedAt,
                coach);
    }
}
