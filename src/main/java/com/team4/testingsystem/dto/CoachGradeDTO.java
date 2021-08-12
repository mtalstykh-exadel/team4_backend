package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.CoachGrade;

import java.io.Serializable;
import java.util.Objects;

public class CoachGradeDTO implements Serializable {
    private Long testId;
    private Long questionId;
    private Integer grade;
    private String comment;

    public CoachGradeDTO() {
    }

    public CoachGradeDTO(CoachGrade coachGrade) {
        this.testId = coachGrade.getId().getTest().getId();
        this.questionId = coachGrade.getId().getQuestion().getId();
        this.grade = coachGrade.getGrade();
        this.comment = coachGrade.getComment();
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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final CoachGradeDTO gradeDTO;

        public Builder() {
            gradeDTO = new CoachGradeDTO();
        }

        public Builder testId(Long testId) {
            gradeDTO.setTestId(testId);
            return this;
        }

        public Builder questionId(Long questionId) {
            gradeDTO.setQuestionId(questionId);
            return this;
        }

        public Builder grade(Integer grade) {
            gradeDTO.setGrade(grade);
            return this;
        }

        public Builder comment(String comment) {
            gradeDTO.setComment(comment);
            return this;
        }

        public CoachGradeDTO build() {
            return gradeDTO;
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
        CoachGradeDTO that = (CoachGradeDTO) o;
        return Objects.equals(testId, that.testId)
                && Objects.equals(questionId, that.questionId)
                && Objects.equals(grade, that.grade)
                && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, questionId, grade, comment);
    }
}
