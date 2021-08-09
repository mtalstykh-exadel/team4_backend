package com.team4.testingsystem.converters;

import static java.util.stream.Collectors.groupingBy;

import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        Long questionId = setQuestions(testDTO, test.getId());
        testDTO.setContentFile(getContentFile(questionId).getUrl());
        return testDTO;
    }

    private ContentFile getContentFile(Long id) {
        return contentFilesService.getContentFileByQuestionId(id);
    }

    private Long setQuestions(TestDTO testDTO, Long id) {
        List<Question> questions = questionService.getQuestionsByTestId(id);
        questions.forEach(question -> Collections.shuffle(question.getAnswers()));
        Map<String, List<QuestionDTO>> map = questions.stream()
                .map(QuestionDTO::create)
                .collect(groupingBy(QuestionDTO::getModule));
        testDTO.setQuestions(map);
        return questions.stream()
                .filter(question1 -> question1.getModule().getName().equals(Modules.LISTENING.getName()))
                .findFirst().orElseThrow(QuestionNotFoundException::new).getId();
    }

}
