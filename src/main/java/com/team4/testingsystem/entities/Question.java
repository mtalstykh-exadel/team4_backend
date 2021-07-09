package com.team4.testingsystem.entities;

import javax.persistence.*;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "question_body")
    private String questionBody;
    private int points;
    @Column(name = "is_avaliable")
    private int isAvailable;

    @OneToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private User creatorUserId;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Level levelID;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Module moduleID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(int isAvaliable) {
        this.isAvailable = isAvaliable;
    }

    public User getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(User creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public Level getLevelID() {
        return levelID;
    }

    public void setLevelID(Level levelID) {
        this.levelID = levelID;
    }

    public Module getModuleID() {
        return moduleID;
    }

    public void setModuleID(Module moduleID) {
        this.moduleID = moduleID;
    }
}
