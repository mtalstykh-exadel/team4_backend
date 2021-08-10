package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.ChosenOption;

import java.io.Serializable;
import java.util.Objects;

public class ChosenOptionDTO implements Serializable {
    private Long testId;
    private Long questionId;
    private Long answerId;

    public ChosenOptionDTO() {
    }

    public ChosenOptionDTO(Long testId, Long questionId, Long answerId) {
        this.testId = testId;
        this.questionId = questionId;
        this.answerId = answerId;
    }

    public ChosenOptionDTO(ChosenOption chosenOption) {
        this.testId = chosenOption.getId().getTest().getId();
        this.questionId = chosenOption.getId().getQuestion().getId();
        this.answerId = chosenOption.getAnswer().getId();
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChosenOptionDTO that = (ChosenOptionDTO) o;
        return Objects.equals(testId, that.testId)
                && Objects.equals(questionId, that.questionId)
                && Objects.equals(answerId, that.answerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, questionId, answerId);
    }
}
