package com.team4.testingsystem.services;

import com.team4.testingsystem.dto.AnswerDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.QuestionStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    Question getById(Long id);

    Question createQuestion(Question question);

    void updateAvailability(Long id, boolean available);

    Question updateQuestion(Question question, Long id);

    List<Question> getRandomQuestions(String level, String module, Pageable pageable);

    List<Question> getRandomQuestionsByContentFile(Long id, Pageable pageable);

    Question addAnswers(Question question, List<AnswerDTO> textAnswers);

    List<Question> getQuestionsByTestId(Long id);

    List<Question> getQuestionsByLevelAndModuleName(Levels level,
                                                    Modules module,
                                                    QuestionStatus status,
                                                    Pageable pageable);

    void archiveQuestionsByContentFileId(Long id);

    Question getQuestionByTestIdAndModule(Long testId, Modules module);

    List<ContentFile> getListening(Levels level, QuestionStatus status, Pageable pageable);
}
