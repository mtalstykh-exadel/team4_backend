package com.team4.testingsystem.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorReportAnswer {
    private ErrorReport report;
    private CoachAnswer answer;
}
