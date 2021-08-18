package com.team4.testingsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team4.testingsystem.enums.Priority;
import com.team4.testingsystem.enums.Status;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "test")
public class Test implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "level_id", referencedColumnName = "id")
    private Level level;

    @Column(name = "assigned_at")
    private Instant assignedAt;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "finish_time")
    private Instant finishTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Column(name = "deadline")
    private Instant deadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @ManyToOne
    @JoinColumn(name = "coach_id", referencedColumnName = "id")
    private User coach;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "test_question",
            joinColumns = @JoinColumn(name = "test_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    List<Question> questions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public User getCoach() {
        return coach;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setQuestion(Question question) {
        this.questions.add(question);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Instant getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Instant finishTime) {
        this.finishTime = finishTime;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
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

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }

    public Boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public void setCoach(User coach) {
        this.coach = coach;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Test test;

        public Builder() {
            this.test = new Test();
        }

        public Builder user(User user) {
            test.user = user;
            return this;
        }

        public Builder assignedAt(Instant assignedAt) {
            test.assignedAt = assignedAt;
            return this;
        }

        public Builder verifiedAt(Instant verifiedAt) {
            test.verifiedAt = verifiedAt;
            return this;
        }

        public Builder startedAt(Instant startedAt) {
            test.startedAt = startedAt;
            return this;
        }

        public Builder completedAt(Instant completedAt) {
            test.completedAt = completedAt;
            return this;
        }

        public Builder finishTime(Instant finishTime) {
            test.finishTime = finishTime;
            return this;
        }

        public Builder deadline(Instant deadline) {
            test.deadline = deadline;
            return this;
        }

        public Builder priority(Priority priority) {
            test.priority = priority;
            return this;
        }

        public Builder status(Status status) {
            test.status = status;
            return this;
        }

        public Builder coach(User coach) {
            test.coach = coach;
            return this;
        }

        public Builder level(Level level) {
            test.level = level;
            return this;
        }

        public Builder isAvailable(Boolean isAvailable) {
            test.isAvailable = isAvailable;
            return this;
        }

        public Test build() {
            return test;
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

        Test test = (Test) o;
        return Objects.equals(id, test.id)
                && Objects.equals(user, test.user)
                && Objects.equals(level, test.level)
                && Objects.equals(assignedAt, test.assignedAt)
                && Objects.equals(verifiedAt, test.verifiedAt)
                && Objects.equals(startedAt, test.startedAt)
                && Objects.equals(completedAt, test.completedAt)
                && Objects.equals(finishTime, test.finishTime)
                && Objects.equals(deadline, test.deadline)
                && Objects.equals(priority, test.priority)
                && Objects.equals(status, test.status)
                && Objects.equals(coach, test.coach)
                && Objects.equals(isAvailable, test.isAvailable)
                && Objects.equals(questions, test.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, level, assignedAt, verifiedAt, startedAt, completedAt, finishTime,
                priority, deadline, status, isAvailable, coach, questions);
    }
}
