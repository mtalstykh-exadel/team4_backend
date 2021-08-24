package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.ErrorReport;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class ReportedQuestionDTO implements Serializable {
    private QuestionDTO question;
    private String report;

    public ReportedQuestionDTO(ErrorReport report) {
        this.question = QuestionDTO.create(report.getId().getQuestion());
        this.report = report.getReportBody();
    }

}
