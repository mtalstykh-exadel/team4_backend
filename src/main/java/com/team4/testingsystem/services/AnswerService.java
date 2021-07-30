package com.team4.testingsystem.services;

import com.team4.testingsystem.dto.AnswerRequest;
import com.team4.testingsystem.entities.Answer;

public interface AnswerService {
    Answer getById(long id);

    void create(AnswerRequest answerRequest);

    void update(long id, AnswerRequest answerRequest);

    void removeById(long id);
}
