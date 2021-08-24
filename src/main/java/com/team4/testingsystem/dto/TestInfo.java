package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.Test;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@Data
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

    private Integer totalScore = 0;

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

    public TestInfo(Test test, Integer totalScore) {
        this(test);
        this.totalScore = totalScore;
    }

}
