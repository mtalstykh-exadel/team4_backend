package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.AnswerNotFoundException;
import com.team4.testingsystem.repositories.AnswerRepository;
import com.team4.testingsystem.services.AnswerService;
import com.team4.testingsystem.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {

   private final AnswerRepository answerRepository;
   private final QuestionService questionService;

    @Autowired
    AnswerServiceImpl(AnswerRepository answerRepository, QuestionService questionService) {
        this.answerRepository = answerRepository;
        this.questionService = questionService;
    }

    @Override
    public Answer getById(long id) {
        return answerRepository.findById(id).orElseThrow(AnswerNotFoundException::new);
    }

    public Iterable<Answer> getAllByQuestion(Question question) {
        return questionService.getQuestionById(question.getId()).getAnswers();
    }
}
