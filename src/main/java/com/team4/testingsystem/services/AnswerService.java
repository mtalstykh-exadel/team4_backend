package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Answer;

public interface AnswerService {
    Answer getById(Long answerId);

    String downloadEssay(Long testId);

    void uploadEssay(Long testId, String text);
}
