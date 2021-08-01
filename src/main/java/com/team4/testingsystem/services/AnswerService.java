package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.Question;

public interface AnswerService {
    Answer getById(long id);

    Iterable<Answer> getAllByQuestion(Question question);
}
