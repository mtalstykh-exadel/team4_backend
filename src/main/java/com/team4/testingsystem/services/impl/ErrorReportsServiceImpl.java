package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.exceptions.ErrorReportAlreadyExistsException;
import com.team4.testingsystem.exceptions.ErrorReportNotFoundException;
import com.team4.testingsystem.repositories.ErrorReportsRepository;
import com.team4.testingsystem.services.ErrorReportsService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
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
    public Collection<ErrorReport> getReportsByTest(Long testId) {
        return errorReportsRepository.findAllByTest(testsService.getById(testId));
    }


    @Override
    public void add(String requestBody, Long questionId, Long testId) {
        Question question = questionService.getQuestionById(questionId);

        Test test = testsService.getById(testId);

        if (errorReportsRepository.findByTestAndQuestion(test, question).isPresent()) {
            throw new ErrorReportAlreadyExistsException();
        }

        errorReportsRepository.save(new ErrorReport(requestBody, question, test));
    }

    public void updateReportBody(long testId, long questionId, String newReportBody) {
        if (errorReportsRepository.changeReportBody(newReportBody,
                testsService.getById(testId),
                questionService.getQuestionById(questionId)) == 0) {
            throw new ErrorReportNotFoundException();
        }
    }

    @Override
    public void removeByTestAndQuestion(long testId, long questionId) {
        if (errorReportsRepository.removeByTestAndQuestion(testsService.getById(testId),
                questionService.getQuestionById(questionId)) == 0) {
            throw new ErrorReportNotFoundException();
        }
    }
}
