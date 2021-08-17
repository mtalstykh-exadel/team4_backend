package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ErrorReport;

import java.util.Collection;
import java.util.List;

public interface ErrorReportsService {

    Collection<ErrorReport> getReportsByTest(Long testId);

    void add(String requestBody, Long questionId, Long testId);

    void removeByTestAndQuestion(long testId, long questionId);

    void addAll(List<ErrorReport> errorReports);
}
