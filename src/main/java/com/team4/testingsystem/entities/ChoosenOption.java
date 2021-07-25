package com.team4.testingsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "choosen_option")
public class ChoosenOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "test_id", nullable = false, referencedColumnName = "id")
    private Test test;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "question_id", nullable = false, referencedColumnName = "id")
    private Question question;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "choosen_answer_id", referencedColumnName = "id")
    private Answer answer;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "file_answer_id", referencedColumnName = "id")
    private FileAnswer fileAnswer;

    public ChoosenOption () {

    }

    public ChoosenOption(Test test, Question question, Answer answer, FileAnswer fileAnswer) {
        this.test = test;
        this.question = question;
        this.answer = answer;
        this.fileAnswer = fileAnswer;
    }

    public Long getId() {
        return id;
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

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public FileAnswer getFileAnswer() {
        return fileAnswer;
    }

    public void setFileAnswer(FileAnswer fileAnswer) {
        this.fileAnswer = fileAnswer;
    }
}

