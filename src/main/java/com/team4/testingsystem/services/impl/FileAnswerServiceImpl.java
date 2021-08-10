package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.exceptions.FileAnswerNotFoundException;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.services.FileAnswerService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileAnswerServiceImpl implements FileAnswerService {
    private final FileAnswerRepository fileAnswerRepository;
    private final QuestionService questionService;
    private final TestsService testsService;

    @Autowired
    public FileAnswerServiceImpl(FileAnswerRepository fileAnswerRepository,
                                 QuestionService questionService,
                                 TestsService testsService) {
        this.fileAnswerRepository = fileAnswerRepository;
        this.questionService = questionService;
        this.testsService = testsService;
    }

    @Override
    public String getUrl(Long testId, Long questionId) {
        FileAnswer fileAnswer = fileAnswerRepository.findByTestAndQuestionId(testId, questionId)
                .orElseThrow(FileAnswerNotFoundException::new);
        return fileAnswer.getUrl();
    }

    @Override
    public void save(Long testId, Long questionId, String url) {
        FileAnswer fileAnswer = FileAnswer.builder()
                .id(createId(testId, questionId))
                .url(url)
                .build();
        fileAnswerRepository.save(fileAnswer);
    }

    @Override
    public void remove(Long testId, Long questionId) {
        fileAnswerRepository.deleteById(createId(testId, questionId));
    }

    private TestQuestionID createId(Long testId, Long questionId) {
        Test test = testsService.getById(testId);
        Question question = questionService.getById(questionId);
        return new TestQuestionID(test, question);
    }
}
