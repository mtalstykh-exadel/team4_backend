package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }


    @Transactional
    @Override
    public void archiveQuestion(Long id) {
        questionRepository.archiveQuestion(id);
    }

    @Transactional
    @Override
    public Question updateQuestion(Question question, Long id) {
        questionRepository.archiveQuestion(id);
        return questionRepository.save(question);
    }

}
