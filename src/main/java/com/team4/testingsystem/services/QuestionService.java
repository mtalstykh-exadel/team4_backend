package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Question;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    Question getById(Long id);

    Question createQuestion(Question question);

    void archiveQuestion(Long id);

    Question updateQuestion(Question question, Long id);

    List<Question> getRandomQuestions(String level, String module, Pageable pageable);

    List<Question> getRandomQuestionsByContentFile(Long id, Pageable pageable);

    List<Question> getQuestionsByTestIdAndModule(Long id, String module);

    Question getQuestionByTestIdAndModule(Long id, String module);
}
