package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.AnswerRequest;
import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.exceptions.AnswerNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.AnswerRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {

    AnswerRepository answerRepository;
    QuestionRepository questionRepository;

    @Autowired
    AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Answer getById(long id) {
        return answerRepository.findById(id).orElseThrow(AnswerNotFoundException::new);
    }

    @Override
    public void create(AnswerRequest answerRequest) {
        Answer answer = Answer.builder().answerBody(answerRequest.getAnswerBody())
                .question(questionRepository.findById(answerRequest.getQuestionId())
                        .orElseThrow(QuestionNotFoundException::new))
                .isCorrect(answerRequest.isCorrect())
                .build();
        answerRepository.save(answer);
    }

    @Override
    public void update(long id, AnswerRequest answerRequest) {
        Answer answer = Answer.builder().id(answerRepository.findById(id)
                .orElseThrow(AnswerNotFoundException::new)
                .getId())
                .answerBody(answerRequest.getAnswerBody())
                .question(questionRepository.findById(answerRequest.getQuestionId())
                        .orElseThrow(QuestionNotFoundException::new))
                .isCorrect(answerRequest.isCorrect())
                .build();
        answerRepository.save(answer);
    }

    @Override
    public void removeById(long id) {
        if (!answerRepository.existsById(id)) {
            throw new AnswerNotFoundException();
        }
        answerRepository.deleteById(id);
    }
}
