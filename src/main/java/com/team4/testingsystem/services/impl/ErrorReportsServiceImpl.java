package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.exceptions.ErrorReportNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.repositories.ErrorReportsRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.services.ErrorReportsService;
import org.springframework.beans.factory.annotation.Autowired;

public class ErrorReportsServiceImpl implements ErrorReportsService {


    private ErrorReportsRepository errorReportsRepository;

    private QuestionRepository questionRepository;

    private TestsRepository testsRepository;

    @Autowired
    public ErrorReportsServiceImpl(ErrorReportsRepository errorReportsRepository,
                                   QuestionRepository questionRepository,
                                   TestsRepository testsRepository) {
        this.errorReportsRepository = errorReportsRepository;
        this.questionRepository = questionRepository;
        this.testsRepository = testsRepository;
    }

    @Override
    public Iterable<ErrorReport> getAll() {
        return errorReportsRepository.findAll();
    }

    @Override
    public ErrorReport getById(long id){
        return errorReportsRepository.findById(id).orElseThrow(ErrorReportNotFoundException::new);
    }

    @Override
    public void add(String requestBody, Long questionId, Long testId){
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        Test test = testsRepository.findById(testId).orElseThrow(TestNotFoundException::new);

        errorReportsRepository.save(new ErrorReport(requestBody, question, test));
    }

    @Override
    public void updateRequestBody(long id, String newRequestBody){
        if (errorReportsRepository.changeReportBody(newRequestBody, id) == 0) {
            throw new ErrorReportNotFoundException();
        }
    }

    @Override
    public void removeById(long id){
        if (errorReportsRepository.removeById(id) == 0) {
            throw new ErrorReportNotFoundException();
        }
    }

}
