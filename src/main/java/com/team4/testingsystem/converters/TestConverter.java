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
        TestDTO testDTO = new TestDTO(test);
        setQuestions(testDTO, test.getId());
        testDTO.setContentFile(appendContentFile(test.getId()).getUrl());
        return testDTO;
    }

    private ContentFile appendContentFile(Long testId) {
        Question question = questionService
                .getQuestionByTestIdAndModule(testId, Modules.LISTENING.getName());
        return contentFilesService.getContentFileByQuestionId(question.getId());
    }

    private void setQuestions(TestDTO testDTO, Long id) {
        testDTO.setGrammarQuestions(getQuestions(id, Modules.GRAMMAR));
        testDTO.setListeningQuestions(getQuestions(id, Modules.LISTENING));
        testDTO.setEssayQuestion(getQuestion(id, Modules.ESSAY));
        testDTO.setSpeakingQuestion(getQuestion(id, Modules.SPEAKING));
    }

    private List<QuestionDTO> getQuestions(Long id, Modules module) {
        return questionService.getQuestionsByTestIdAndModule(id, module.getName())
                .stream()
                .map(QuestionDTO::new)
                .collect(Collectors.toList());
    }

    private QuestionDTO getQuestion(Long id, Modules module) {
        Question question = questionService
                .getQuestionByTestIdAndModule(id, module.getName());
        return new QuestionDTO(question);
    }
}
