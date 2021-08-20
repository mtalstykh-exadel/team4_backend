package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.AnswerDTO;
import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.QuestionStatus;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.RestrictionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ContentFilesRepository contentFilesRepository;
    private final RestrictionsService restrictionsService;

    @Autowired
    QuestionServiceImpl(QuestionRepository questionRepository,
                        ContentFilesRepository contentFilesRepository,
                        RestrictionsService restrictionsService) {
        this.questionRepository = questionRepository;
        this.contentFilesRepository = contentFilesRepository;
        this.restrictionsService = restrictionsService;
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
        restrictionsService.checkAnswersAreCorrect(answers);
        question.setAnswers(answers);
        return questionRepository.save(question);
    }

    @Transactional
    @Override
    public void updateAvailability(Long id, boolean available) {
        Question question = getById(id);
        restrictionsService.checkModuleIsNotListening(question);
        questionRepository.updateAvailability(id, available);
    }

    @Transactional
    @Override
    public Question updateQuestion(Question question, Long id) {
        questionRepository.updateAvailability(id, false);
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
    public List<Question> getQuestionsByLevelAndModuleName(Levels level,
                                                           Modules module,
                                                           QuestionStatus status,
                                                           Pageable pageable) {
        boolean isAvailable = status.getName().equals(QuestionStatus.UNARCHIVED.getName());
        return questionRepository.getQuestionsByLevelAndModuleName(level.name(),
                module.getName(),
                isAvailable,
                pageable);
    }

    @Override
    public Question getQuestionByTestIdAndModule(Long testId, Modules module) {
        return questionRepository.getQuestionByTestIdAndModule(testId, module.getName())
                .orElseThrow(QuestionNotFoundException::new);
    }

    @Override
    public List<ContentFile> getListening(Levels level, QuestionStatus status, Pageable pageable) {
        boolean isAvailable = status.getName().equals(QuestionStatus.UNARCHIVED.getName());
        if (level == null) {
            return contentFilesRepository.findAllByAvailableOrderByIdDesc(isAvailable, pageable);
        }
        return contentFilesRepository.getContentFiles(level.name(), isAvailable, pageable);
    }

    @Transactional
    @Override
    public void archiveQuestionsByContentFileId(Long id) {
        ContentFile contentFile = contentFilesRepository.findById(id)
                .orElseThrow(FileNotFoundException::new);
        contentFile.getQuestions().forEach(question ->
                questionRepository.updateAvailability(question.getId(), false));
    }
}
