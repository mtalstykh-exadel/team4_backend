package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question getById(Long id) {
        return questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
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

    @Override
    public List<Question> getRandomQuestions(String level, String module, Pageable pageable) {
        return questionRepository.getRandomQuestions(level, module, pageable);
    }

    @Override
    public List<Question> getRandomQuestionsByContentFile(Long id, Pageable pageable) {
        return questionRepository.getRandomQuestionByContentFile(id, pageable);
    }

    @Override
    public List<Question> getQuestionsByTestIdAndModule(Long id, String module) {
        return questionRepository.getQuestionsByTestIdAndModule(id, module);
    }

    @Override
    public Question getQuestionByTestIdAndModule(Long id, String module) {
        return questionRepository
                .getQuestionByTestIdAndModule(id, module).orElseThrow(QuestionNotFoundException::new);
    }

}
