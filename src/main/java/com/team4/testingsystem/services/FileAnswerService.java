package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.enums.Modules;
import org.springframework.web.multipart.MultipartFile;

public interface FileAnswerService {
    String getUrl(Long testId, Long questionId);

    FileAnswer uploadSpeaking(MultipartFile file, Long testId);

    String downloadSpeaking(Long testId);

    void remove(Long testId, Long questionId);

    String downloadEssay(Long testId);

    FileAnswer uploadEssay(Long testId, String text);

}
