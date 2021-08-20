package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.exceptions.AnswerNotFoundException;
import com.team4.testingsystem.exceptions.FileAnswerNotFoundException;
import com.team4.testingsystem.repositories.AnswerRepository;
import com.team4.testingsystem.services.AnswerService;
import com.team4.testingsystem.services.FileAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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
    public Optional<String> tryDownloadEssay(Long testId) {
        try {
            return Optional.of(downloadEssay(testId));
        } catch (FileAnswerNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public String uploadEssay(Long testId, String text) {
        return fileAnswerService.uploadEssay(testId, text).getUrl();
    }

    @Override
    public String downloadSpeaking(Long testId) {
        return fileAnswerService.downloadSpeaking(testId);
    }

    @Override
    public Optional<String> tryDownloadSpeaking(Long testId) {
        try {
            return Optional.of(downloadSpeaking(testId));
        } catch (FileAnswerNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public String uploadSpeaking(MultipartFile file, Long testId) {
        return fileAnswerService.uploadSpeaking(file, testId).getUrl();
    }
}
