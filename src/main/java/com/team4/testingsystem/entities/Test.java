package com.team4.testingsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team4.testingsystem.enums.Priority;
import com.team4.testingsystem.enums.Status;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
@Getter
@Setter
@EqualsAndHashCode
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

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public void addAllQuestions(List<Question> questions) {
        this.questions.addAll(questions);
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

}
