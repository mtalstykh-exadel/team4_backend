package com.team4.testingsystem.services;

import com.team4.testingsystem.model.entity.Answer;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface AnswerService {
    Answer getById(Long answerId);

    String downloadEssay(Long testId);

    Optional<String> tryDownloadEssay(Long testId);

    String uploadEssay(Long testId, String text);

    String downloadSpeaking(Long testId);

    Optional<String> tryDownloadSpeaking(Long testId);

    String uploadSpeaking(MultipartFile file, Long testId);
}
