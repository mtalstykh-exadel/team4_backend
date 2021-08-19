package com.team4.testingsystem.dto;

import java.util.Objects;

public class CoachAnswerDTO {
    private Long questionId;
    private Long testId;
    private String comment;

    public CoachAnswerDTO(Long questionId, Long testId, String comment) {
        this.questionId = questionId;
        this.testId = testId;
        this.comment = comment;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CoachAnswerDTO that = (CoachAnswerDTO) o;
        return Objects.equals(questionId, that.questionId)
                && Objects.equals(testId, that.testId)
                && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, testId, comment);
    }
}
