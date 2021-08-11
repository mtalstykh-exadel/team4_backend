package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;

import java.io.Serializable;

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
}
