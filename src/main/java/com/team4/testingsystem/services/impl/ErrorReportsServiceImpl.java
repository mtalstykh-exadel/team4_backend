package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.exceptions.ErrorReportNotFoundException;
import com.team4.testingsystem.repositories.ErrorReportsRepository;
import com.team4.testingsystem.services.ErrorReportsService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;

public class ErrorReportsServiceImpl implements ErrorReportsService {


    private ErrorReportsRepository errorReportsRepository;

    private QuestionService questionService;

    private TestsService testsService;

    @Autowired
    public ErrorReportsServiceImpl(ErrorReportsRepository errorReportsRepository,
                                   QuestionService questionService,
                                   TestsService testsService) {
        this.errorReportsRepository = errorReportsRepository;
        this.questionService = questionService;
        this.testsService = testsService;
    }

    @Override
    public Iterable<ErrorReport> getAll() {
        return errorReportsRepository.findAll();
    }

    @Override
    public ErrorReport getById(long id) {
        return errorReportsRepository.findById(id).orElseThrow(ErrorReportNotFoundException::new);
    }

    @Override
    public void add(String requestBody, Long questionId, Long testId) {
        Question question = questionService.getQuestionById(questionId);

        Test test = testsService.getById(testId);

        errorReportsRepository.save(new ErrorReport(requestBody, question, test));
    }

    @Override
    public void updateRequestBody(long id, String newRequestBody) {
        if (errorReportsRepository.changeReportBody(newRequestBody, id) == 0) {
            throw new ErrorReportNotFoundException();
        }
    }

    @Override
    public void removeById(long id) {
        if (errorReportsRepository.removeById(id) == 0) {
            throw new ErrorReportNotFoundException();
        }
    }
}
