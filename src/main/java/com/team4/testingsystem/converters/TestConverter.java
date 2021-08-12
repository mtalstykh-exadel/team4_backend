package com.team4.testingsystem.converters;

import static java.util.stream.Collectors.groupingBy;

import com.team4.testingsystem.dto.ContentFileDTO;
import com.team4.testingsystem.dto.ListeningTopicRequest;
import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.ContentFileNotFoundException;
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
        attachQuestions(testDTO);
        attachContentFile(testDTO);
        return testDTO;
    }

    private void attachQuestions(TestDTO testDTO) {
        Map<String, List<QuestionDTO>> questions = questionService.getQuestionsByTestId(testDTO.getId()).stream()
                .peek(question -> Collections.shuffle(question.getAnswers()))
                .map(QuestionDTO::create)
                .collect(groupingBy(QuestionDTO::getModule));

        testDTO.setQuestions(questions);
    }

    private void attachContentFile(TestDTO testDTO) {
        final ContentFile contentFile = testDTO.getQuestions()
                .getOrDefault(Modules.LISTENING.getName(), List.of()).stream()
                .map(QuestionDTO::getId)
                .map(contentFilesService::getContentFileByQuestionId)
                .findFirst()
                .orElseThrow(ContentFileNotFoundException::new);

        testDTO.setContentFile(new ListeningTopicRequest(contentFile));
    }
}
