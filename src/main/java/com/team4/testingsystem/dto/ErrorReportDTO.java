package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.ErrorReport;

import java.io.Serializable;
import java.util.Objects;

public class ErrorReportDTO implements Serializable {

    private String reportBody;

    private long questionId;

    private long testId;

    public ErrorReportDTO(){
    }

    public ErrorReportDTO(ErrorReport errorReport) {
        this.reportBody = errorReport.getReportBody();
        this.questionId = errorReport.getQuestion().getId();
        this.testId = errorReport.getTest().getId();
    }

    public String getReportBody() {
        return reportBody;
    }

    public void setReportBody(String reportBody) {
        this.reportBody = reportBody;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public static ErrorReportDTO.Builder builder() {
        return new ErrorReportDTO.Builder();
    }

    public static class Builder {
        private final ErrorReportDTO errorReportDTO;

        public Builder() {
            errorReportDTO = new ErrorReportDTO();
        }

        public ErrorReportDTO.Builder testId(Long testId) {
            errorReportDTO.setTestId(testId);
            return this;
        }

        public ErrorReportDTO.Builder questionId(Long questionId) {
            errorReportDTO.setQuestionId(questionId);
            return this;
        }

        public ErrorReportDTO.Builder reportBody(String reportBody) {
            errorReportDTO.setReportBody(reportBody);
            return this;
        }

        public ErrorReportDTO build() {
            return errorReportDTO;
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
        ErrorReportDTO that = (ErrorReportDTO) o;
        return Objects.equals(testId, that.testId)
                && Objects.equals(questionId, that.questionId)
                && Objects.equals(reportBody, that.reportBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, questionId, reportBody);
    }

}
