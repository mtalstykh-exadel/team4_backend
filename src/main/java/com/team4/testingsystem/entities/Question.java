package com.team4.testingsystem.entities;

import javax.persistence.*;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "question_body")
    private String questionBody;
    @Column(name = "is_available")
    private boolean isAvailable;

    @OneToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Level level;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Module module;

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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
