package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.FileAnswerNotFoundException;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.services.FileAnswerService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.ResourceStorageService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileAnswerServiceImpl implements FileAnswerService {
    private final FileAnswerRepository fileAnswerRepository;
    private final QuestionService questionService;
    private final TestsService testsService;
    private final ResourceStorageService storageService;

    @Autowired
    public FileAnswerServiceImpl(FileAnswerRepository fileAnswerRepository,
                                 QuestionService questionService,
                                 TestsService testsService,
                                 ResourceStorageService storageService) {
        this.fileAnswerRepository = fileAnswerRepository;
        this.questionService = questionService;
        this.testsService = testsService;
        this.storageService = storageService;
    }

    @Override
    public String getUrl(Long testId, Long questionId) {
        FileAnswer fileAnswer = fileAnswerRepository.findByTestAndQuestionId(testId, questionId)
                .orElseThrow(FileAnswerNotFoundException::new);
        return fileAnswer.getUrl();
    }

    @Override
    public FileAnswer addFileAnswer(MultipartFile file, Long testId, Modules module) {
        String url = storageService.upload(file.getResource());
        Question question = questionService.getQuestionByTestIdAndModule(testId, module);
        return save(testId, question.getId(), url);
    }

    @Override
    public FileAnswer save(Long testId, Long questionId, String url) {
        FileAnswer fileAnswer = FileAnswer.builder()
                .id(createId(testId, questionId))
                .url(url)
                .build();
        return fileAnswerRepository.save(fileAnswer);
    }

    @Override
    public void remove(Long testId, Long questionId) {
        fileAnswerRepository.deleteById(createId(testId, questionId));
    }

    @Override
    public String downloadEssay(Long testId) {
        Test test = testsService.getById(testId);
        return null;
    }

    @Override
    public String getSpeaking(Long testId) {
        Question question = questionService
                .getQuestionByTestIdAndModule(testId, Modules.SPEAKING);
        return getUrl(testId, question.getId());
    }

    private TestQuestionID createId(Long testId, Long questionId) {
        Test test = testsService.getById(testId);
        Question question = questionService.getById(questionId);
        return new TestQuestionID(test, question);
    }
}
