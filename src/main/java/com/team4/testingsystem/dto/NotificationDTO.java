package com.team4.testingsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.enums.Priority;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder(builderClassName = "Builder")
@EqualsAndHashCode
public class NotificationDTO {
    private Long id;
    private NotificationType type;
    private Long testId;
    private Instant createdAt;

    private String level;
    private Instant finishTime;
    private Instant deadline;
    private Priority priority;

    private String userEmail;
    private String userName;
    private String language;
}
