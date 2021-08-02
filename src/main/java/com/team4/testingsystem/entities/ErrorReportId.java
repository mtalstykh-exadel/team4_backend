package com.team4.testingsystem.entities;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import javax.persistence.PrimaryKeyJoinColumn;

@Embeddable
public class ErrorReportId implements Serializable {

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    public ErrorReportId() {
    }

    public ErrorReportId(Test test, Question question) {
        this.test = test;
        this.question = question;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}