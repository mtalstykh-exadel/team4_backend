package com.team4.testingsystem.dto;

import java.io.Serializable;
import java.util.Objects;

public class CoachGradeRequest implements Serializable {
    private Long testId;
    private Long questionId;
    private Integer grade;

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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final CoachGradeRequest gradeRequest;

        public Builder() {
            gradeRequest = new CoachGradeRequest();
        }

        public Builder testId(Long testId) {
            gradeRequest.setTestId(testId);
            return this;
        }

        public Builder questionId(Long questionId) {
            gradeRequest.setQuestionId(questionId);
            return this;
        }

        public Builder grade(Integer grade) {
            gradeRequest.setGrade(grade);
            return this;
        }

        public CoachGradeRequest build() {
            return gradeRequest;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CoachGradeRequest that = (CoachGradeRequest) o;
        return Objects.equals(testId, that.testId)
                && Objects.equals(questionId, that.questionId)
                && Objects.equals(grade, that.grade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, questionId, grade);
    }
}
