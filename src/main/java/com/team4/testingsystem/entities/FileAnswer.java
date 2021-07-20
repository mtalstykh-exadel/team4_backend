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
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Question question;

    @Column(name = "url")
    private String url;

    public FileAnswer() {
    }

    public static Builder newBuilder() {
        return new FileAnswer().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder setId(long id) {
            FileAnswer.this.id = id;
            return this;
        }

        public Builder setQuestion(Question question) {
            FileAnswer.this.question = question;
            return this;
        }

        public Builder setUrl(String url) {
            FileAnswer.this.url = url;
            return this;
        }

        public FileAnswer build() {
            return FileAnswer.this;
        }
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
