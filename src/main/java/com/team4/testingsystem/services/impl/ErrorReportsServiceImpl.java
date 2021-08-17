package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.exceptions.ErrorReportNotFoundException;
import com.team4.testingsystem.repositories.ErrorReportsRepository;
import com.team4.testingsystem.services.ErrorReportsService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ErrorReportsServiceImpl implements ErrorReportsService {

    private final ErrorReportsRepository errorReportsRepository;

    private final QuestionService questionService;

    private final TestsService testsService;

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
        return errorReportsRepository.findAllById_Test(testsService.getById(testId));
    }

    @Override
    public void add(String reportBody, Long questionId, Long testId) {
        Question question = questionService.getById(questionId);

        Test test = testsService.getById(testId);

        TestQuestionID errorReportId = new TestQuestionID(test, question);

        errorReportsRepository.save(new ErrorReport(errorReportId, reportBody));
    }

    @Override
    public void removeByTestAndQuestion(long testId, long questionId) {
        Question question = questionService.getById(questionId);

        Test test = testsService.getById(testId);

        TestQuestionID errorReportId = new TestQuestionID(test, question);

        if (errorReportsRepository.removeById(errorReportId) == 0) {
            throw new ErrorReportNotFoundException();
        }
    }

    @Override
    public void addAll(List<ErrorReport> errorReports) {
        errorReportsRepository.saveAll(errorReports);
    }
}
