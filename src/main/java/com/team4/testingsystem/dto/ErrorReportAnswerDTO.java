package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.ErrorReportAnswer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class ErrorReportAnswerDTO implements Serializable {
    private String question;
    private String report;
    private String answer;

    public ErrorReportAnswerDTO(ErrorReportAnswer errorReportAnswer) {
        this.question = errorReportAnswer.getReport().getId().getQuestion().getBody();
        this.report = errorReportAnswer.getReport().getReportBody();
        if (errorReportAnswer.getAnswer() != null) {
            this.answer = errorReportAnswer.getAnswer().getComment();
        }
    }
}
