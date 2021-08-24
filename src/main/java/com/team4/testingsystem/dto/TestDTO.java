package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.Test;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
public class TestDTO implements Serializable {
    private Long id;
    private String level;
    private Instant assignedAt;
    private Instant completedAt;
    private Instant verifiedAt;
    private Instant startedAt;
    private Instant deadline;
    private Instant finishTime;
    private Integer listeningAttempts;

    private String priority;
    private String status;
    private UserDTO coach;
    private Map<String, List<QuestionDTO>> questions;
    private ListeningTopicDTO contentFile;

    private String essayText;
    private String speakingUrl;

    private List<ErrorReportDTO> errorReports;

    public TestDTO(Test test) {
        id = test.getId();
        level = test.getLevel().getName();
        assignedAt = test.getAssignedAt();
        completedAt = test.getCompletedAt();
        verifiedAt = test.getVerifiedAt();
        startedAt = test.getStartedAt();
        finishTime = test.getFinishTime();
        deadline = test.getDeadline();
        priority = test.getPriority().getName();
        status = test.getStatus().name();
        listeningAttempts = test.getListeningAttempts();

        if (test.getCoach() != null) {
            coach = new UserDTO(test.getCoach());
        }
    }

}
