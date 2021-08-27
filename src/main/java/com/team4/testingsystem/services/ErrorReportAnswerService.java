package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ErrorReportAnswer;

import java.util.List;

public interface ErrorReportAnswerService {
    List<ErrorReportAnswer> getReportsWithAnswersByTest(Long testId);
}
