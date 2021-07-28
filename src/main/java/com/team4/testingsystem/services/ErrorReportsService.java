package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ErrorReport;

public interface ErrorReportsService{

        Iterable<ErrorReport> getAll();

        ErrorReport getById(long id);

        void add(String requestBody, Long questionId, Long testId);

        void updateRequestBody(long id, String newRequestBody);

        void removeById(long id);
}
