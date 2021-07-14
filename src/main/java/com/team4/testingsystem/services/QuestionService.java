package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Question;

public interface QuestionService {
    Question getQuestionById(Long id);
    Question createQuestion(Question question);
    void archiveQuestion(boolean isAvailable, Long id);
    Question updateQuestion(Question question, Long id);
}
