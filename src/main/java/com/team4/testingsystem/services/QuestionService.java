package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Question;

public interface QuestionService {
    Question getQuestionById(Long id);
    Question createQuestion(String questionBody, boolean isAvailable, Long creatorUserId, Long levelID, Long moduleID);
    Question updateAvailability(Long id, boolean isAvailable);
}
