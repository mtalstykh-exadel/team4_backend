package com.team4.testingsystem.entities;

import javax.persistence.*;

@Entity
@Table(name = "file_answer")
public class FileAnswer{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Question question;

    @Column(name = "url")
    private String url;

    public FileAnswer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
