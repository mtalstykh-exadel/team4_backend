package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;

import java.io.Serializable;
import java.util.Objects;

public class ReportedQuestionDTO implements Serializable {
    private QuestionDTO question;
    private String report;

    public ReportedQuestionDTO() {
    }

    public ReportedQuestionDTO(ErrorReport report) {
        this.question = QuestionDTO.create(report.getId().getQuestion());
        this.report = report.getReportBody();
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReportedQuestionDTO that = (ReportedQuestionDTO) o;
        return Objects.equals(question, that.question)
                && Objects.equals(report, that.report);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, report);
    }
}
