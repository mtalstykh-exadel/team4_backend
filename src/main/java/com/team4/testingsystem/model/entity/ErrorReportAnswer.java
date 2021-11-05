package com.team4.testingsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorReportAnswer {
    private ErrorReport report;
    private CoachAnswer answer;
}
