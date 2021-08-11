package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.enums.Modules;
import org.springframework.web.multipart.MultipartFile;

public interface FileAnswerService {
    String getUrl(Long testId, Long questionId);

    FileAnswer addFileAnswer(MultipartFile file, Long testId, Modules module);

    FileAnswer save(Long testId, Long questionId, String url);

    void remove(Long testId, Long questionId);

    String downloadEssay(Long testId);

    void uploadEssay(Long testId, String text);

    String getSpeaking(Long testId);
}
