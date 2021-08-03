package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestConverter {

    private final QuestionService questionService;
    private final ContentFilesService contentFilesService;

    @Autowired
    public TestConverter(QuestionService questionService,
                         ContentFilesService contentFilesService) {
        this.questionService = questionService;
        this.contentFilesService = contentFilesService;
    }

    public TestDTO convertToDTO(Test test) {
        TestDTO testDTO = new TestDTO(
                test.getLevel().getName(),
                test.getCreatedAt(),
                test.getFinishedAt());
        testDTO.setGrammarQuestions(getQuestions(test.getId(), Modules.GRAMMAR));
        testDTO.setListeningQuestions(getQuestions(test.getId(), Modules.LISTENING));
        testDTO.setEssayQuestion(getQuestion(test.getId(), Modules.ESSAY));
        testDTO.setSpeakingQuestion(getQuestion(test.getId(), Modules.SPEAKING));
        Question question = questionService
                .getQuestionByTestIdAndModule(test.getId(), Modules.LISTENING.getName());
        ContentFile contentFile = contentFilesService
                .getContentFileByQuestionId(question.getId());
        testDTO.setContentFile(contentFile.getUrl());
        return testDTO;
    }

    private List<QuestionDTO> getQuestions(Long id, Modules module) {
        List<Question> questions = questionService
                .getQuestionsByTestIdAndModule(id, module.getName());
        return questions.stream().map(QuestionDTO::new).collect(Collectors.toList());
    }

    private QuestionDTO getQuestion(Long id, Modules module) {
        Question question = questionService
                .getQuestionByTestIdAndModule(id, module.getName());
        return new QuestionDTO(question);
    }
}
