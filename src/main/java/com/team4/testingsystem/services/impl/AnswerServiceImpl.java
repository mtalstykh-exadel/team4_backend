package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.exceptions.AnswerNotFoundException;
import com.team4.testingsystem.repositories.AnswerRepository;
import com.team4.testingsystem.services.AnswerService;
import com.team4.testingsystem.services.FileAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final FileAnswerService fileAnswerService;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository,
                             FileAnswerService fileAnswerService) {
        this.answerRepository = answerRepository;
        this.fileAnswerService = fileAnswerService;
    }

    @Override
    public Answer getById(Long answerId) {
        return answerRepository.findById(answerId).orElseThrow(AnswerNotFoundException::new);
    }

    @Override
    public String downloadEssay(Long testId) {
        return fileAnswerService.downloadEssay(testId);
    }

    @Override
    public void uploadEssay(Long testId, String text) {
        fileAnswerService.uploadEssay(testId, text);
    }
}
