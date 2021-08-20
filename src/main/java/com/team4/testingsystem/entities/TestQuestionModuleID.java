package com.team4.testingsystem.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Embeddable
public class TestQuestionModuleID implements Serializable {

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "module_id", referencedColumnName = "id")
    private Module module;

    public TestQuestionModuleID(){

    }

    public TestQuestionModuleID(TestQuestionID testQuestionID, Module module) {
        test = testQuestionID.getTest();
        question = testQuestionID.getQuestion();
        this.module = module;
    }

    public TestQuestionModuleID(Test test, Question question, Module module) {
        this.test = test;
        this.question = question;
        this.module = module;
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

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestQuestionModuleID that = (TestQuestionModuleID) o;
        return Objects.equals(test, that.test)
                && Objects.equals(question, that.question)
                && Objects.equals(module, that.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(test, question, module);
    }
}
