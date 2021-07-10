package com.team4.testingsystem.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.team4.testingsystem.serializers.UserSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @ManyToOne
    @JsonSerialize(using = UserSerializer.class)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime  createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime  getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime  updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime  getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime  startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime  getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime  finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }


}
