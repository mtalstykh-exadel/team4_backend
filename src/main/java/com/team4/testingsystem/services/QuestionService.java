package com.team4.testingsystem.services;

import com.team4.testingsystem.dto.AnswerDTO;
import com.team4.testingsystem.dto.ContentFileDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    Question getById(Long id);

    Question createQuestion(Question question);

    void archiveQuestion(Long id);

    Question updateQuestion(Question question, Long id);

    List<Question> getRandomQuestions(String level, String module, Pageable pageable);

    List<Question> getRandomQuestionsByContentFile(Long id, Pageable pageable);

    Question addAnswers(Question question, List<AnswerDTO> textAnswers);

    List<Question> getQuestionsByTestId(Long id);

    List<Question> getQuestionsByLevelAndModuleName(Levels level, Modules module);

    void archiveQuestionsByContentFileId(Long id);

    Question getQuestionByTestIdAndModule(Long testId, Modules module);

    List<ContentFile> getListening(Levels level);
}
