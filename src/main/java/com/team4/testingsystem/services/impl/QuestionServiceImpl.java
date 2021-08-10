package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.AnswerDTO;
import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
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
    public Question addAnswers(Question question, List<AnswerDTO> textAnswers) {
        List<Answer> answers = textAnswers.stream()
                .map(answerDTO -> new Answer(answerDTO, question))
                .collect(Collectors.toList());
        question.setAnswers(answers);
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
    public List<Question> getQuestionsByTestId(Long id) {
        return questionRepository.getQuestionsByTestId(id);
    }

    @Override
    public List<Question> getQuestionsByLevelAndModuleName(Levels level, Modules module) {
        return questionRepository.getQuestionsByLevelAndModuleName(level, module);
    }

}
