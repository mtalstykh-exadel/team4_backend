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
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "answer_body")
    private String answerBody;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Question question;

    @Column(name = "is_correct")
    private boolean isCorrect;

    public Answer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAnswerBody() {
        return answerBody;
    }

    public void setAnswerBody(String answerBody) {
        this.answerBody = answerBody;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Answer answer;

        public Builder() {
            answer = new Answer();
        }

        public Builder id(long id) {
            answer.id = id;
            return this;
        }

        public Builder answerBody(String answerBody) {
            answer.answerBody = answerBody;
            return this;
        }

        public Builder question(Question question) {
            answer.question = question;
            return this;
        }

        public Builder isCorrect(boolean isCorrect) {
            answer.isCorrect = isCorrect;
            return this;
        }

        public Answer build() {
            return answer;
        }
    }
}
