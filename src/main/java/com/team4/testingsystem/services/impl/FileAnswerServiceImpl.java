package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.FileAnswerNotFoundException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.services.FileAnswerService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.ResourceStorageService;
import com.team4.testingsystem.services.TestsService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class FileAnswerServiceImpl implements FileAnswerService {
    private final FileAnswerRepository fileAnswerRepository;
    private final QuestionService questionService;
    private final TestsService testsService;
    private final ResourceStorageService resourceStorageService;

    @Autowired
    public FileAnswerServiceImpl(FileAnswerRepository fileAnswerRepository,
                                 QuestionService questionService,
                                 TestsService testsService,
                                 ResourceStorageService resourceStorageService) {
        this.fileAnswerRepository = fileAnswerRepository;
        this.questionService = questionService;
        this.testsService = testsService;
        this.resourceStorageService = resourceStorageService;
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

    @Override
    public String downloadEssay(Long testId) {
        Question question = questionService.getQuestionByTestIdAndModule(testId, Modules.ESSAY);
        String url = getUrl(testId, question.getId());
        try {
            return IOUtils.toString(resourceStorageService.load(url).getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileLoadingFailedException();
        }
    }

    @Override
    public void uploadEssay(Long testId, String text) {
        Test test = testsService.getById(testId);
        Question question = questionService.getQuestionByTestIdAndModule(testId, Modules.ESSAY);

        InputStream inputStream = IOUtils.toInputStream(text, StandardCharsets.UTF_8);
        String url = resourceStorageService.upload(new InputStreamResource(inputStream));

        FileAnswer fileAnswer = FileAnswer.builder()
                .id(new TestQuestionID(test, question))
                .url(url)
                .build();
        fileAnswerRepository.save(fileAnswer);
    }

    private TestQuestionID createId(Long testId, Long questionId) {
        Test test = testsService.getById(testId);
        Question question = questionService.getById(questionId);
        return new TestQuestionID(test, question);
    }
}
