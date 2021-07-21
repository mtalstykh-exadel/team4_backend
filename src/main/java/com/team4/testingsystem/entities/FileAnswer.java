package com.team4.testingsystem.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "file_answer")
public class FileAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, referencedColumnName = "id")
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final FileAnswer fileAnswer;

        public Builder() {
            this.fileAnswer = new FileAnswer();
        }

        public Builder id(long id) {
            fileAnswer.id = id;
            return this;
        }

        public Builder question(Question question) {
            fileAnswer.question = question;
            return this;
        }

        public Builder url(String url) {
            fileAnswer.url = url;
            return this;
        }

        public FileAnswer build() {
            return fileAnswer;
        }
    }
}
