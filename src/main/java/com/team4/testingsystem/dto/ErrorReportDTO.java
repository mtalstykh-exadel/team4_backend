package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.ErrorReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ErrorReportDTO implements Serializable {

    private String reportBody;

    private long questionId;

    private long testId;

    public ErrorReportDTO(ErrorReport errorReport) {
        this.reportBody = errorReport.getReportBody();
        this.questionId = errorReport.getId().getQuestion().getId();
        this.testId = errorReport.getId().getTest().getId();
    }

}
