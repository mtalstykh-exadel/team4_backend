package com.team4.testingsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.enums.Priority;

import java.time.Instant;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO {
    private Long id;
    private NotificationType type;
    private Long testId;
    private Instant createdAt;

    private String level;
    private Instant finishTime;
    private Instant deadline;
    private Priority priority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Instant getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Instant finishTime) {
        this.finishTime = finishTime;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final NotificationDTO notificationDTO;

        public Builder() {
            notificationDTO = new NotificationDTO();
        }

        public Builder id(Long id) {
            notificationDTO.setId(id);
            return this;
        }

        public Builder type(NotificationType type) {
            notificationDTO.setType(type);
            return this;
        }

        public Builder testId(Long testId) {
            notificationDTO.setTestId(testId);
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            notificationDTO.setCreatedAt(createdAt);
            return this;
        }

        public Builder level(String level) {
            notificationDTO.setLevel(level);
            return this;
        }

        public Builder finishTime(Instant finishTime) {
            notificationDTO.setFinishTime(finishTime);
            return this;
        }

        public Builder deadline(Instant deadline) {
            notificationDTO.setDeadline(deadline);
            return this;
        }

        public Builder priority(Priority priority) {
            notificationDTO.setPriority(priority);
            return this;
        }

        public NotificationDTO build() {
            return notificationDTO;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationDTO that = (NotificationDTO) o;
        return Objects.equals(id, that.id)
                && type == that.type
                && Objects.equals(testId, that.testId)
                && Objects.equals(level, that.level)
                && Objects.equals(finishTime, that.finishTime)
                && Objects.equals(deadline, that.deadline)
                && Objects.equals(priority, that.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, testId, level, finishTime, deadline, priority);
    }
}
