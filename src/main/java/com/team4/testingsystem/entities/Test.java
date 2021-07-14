package com.team4.testingsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id", updatable = false)
    private User user;

    @Column(name = "created_at")
    private LocalDateTime  createdAt;

    @Column(name = "updated_at")
    private LocalDateTime  updatedAt;

    @Column(name = "started_at")
    private LocalDateTime  startedAt;

    @Column(name = "finished_at")
    private LocalDateTime  finishedAt;

    @Column(name = "status")
    private String status;

    @Column(name = "evaluation")
    private int evaluation;

    private Test(){
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime  getUpdatedAt() {
        return updatedAt;
    }


    public LocalDateTime  getStartedAt() {
        return startedAt;
    }

    public LocalDateTime  getFinishedAt() {
        return finishedAt;
    }


    public String getStatus() {
        return status;
    }


    public int getEvaluation() {
        return evaluation;
    }


    public Builder builder() {
        return this.new Builder();
    }

    public static Test.Builder newBuilder() {
        return new Test().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Test.Builder setUser(User user) {
            Test.this.user = user;
            return this;
        }

        public Test.Builder setCreatedAt(LocalDateTime createdAt) {
            Test.this.createdAt = createdAt;
            return this;
        }

        public Test.Builder setUpdatedAt(LocalDateTime updatedAt) {
            Test.this.updatedAt = updatedAt;
            return this;
        }

        public Test.Builder setStartedAt(LocalDateTime startedAt) {
            Test.this.startedAt = startedAt;
            return this;
        }

        public Test.Builder setFinishedAt(LocalDateTime finishedAt) {
            Test.this.finishedAt = finishedAt;
            return this;
        }

        public Test.Builder setStatus(String status) {
            Test.this.status = status;
            return this;
        }

        public Test.Builder setEvaluation(int evaluation) {
            Test.this.evaluation = evaluation;
            return this;
        }

        public Test build() {
            return Test.this;
        }


    }
}
