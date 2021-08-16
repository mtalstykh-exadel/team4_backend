package com.team4.testingsystem.converters;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import com.team4.testingsystem.dto.ListeningTopicDTO;
import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.ContentFileNotFoundException;
import com.team4.testingsystem.services.ChosenOptionService;
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
    private final ChosenOptionService chosenOptionService;

    @Autowired
    public TestConverter(QuestionService questionService,
                         ContentFilesService contentFilesService,
                         ChosenOptionService chosenOptionService) {
        this.questionService = questionService;
        this.contentFilesService = contentFilesService;
        this.chosenOptionService = chosenOptionService;
    }

    public TestDTO convertToDTO(Test test) {
        TestDTO testDTO = new TestDTO(test);
        attachQuestions(testDTO);
        attachContentFile(testDTO);
        return testDTO;
    }

    private void attachQuestions(TestDTO testDTO) {
        Map<Long, Long> chosenOptions = chosenOptionService.getAllByTestId(testDTO.getId()).stream()
                .collect(toMap(option -> option.getId().getQuestion().getId(), option -> option.getAnswer().getId()));

        Map<String, List<QuestionDTO>> questions = questionService.getQuestionsByTestId(testDTO.getId()).stream()
                .peek(question -> Collections.shuffle(question.getAnswers()))
                .map(QuestionDTO::create)
                .peek(question -> markChosenOption(question, chosenOptions.getOrDefault(question.getId(), null)))
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

        testDTO.setContentFile(new ListeningTopicDTO(contentFile));
    }

    private void markChosenOption(QuestionDTO questionDTO, Long chosenAnswerId) {
        questionDTO.getAnswers().stream()
                .filter(answer -> answer.getId().equals(chosenAnswerId))
                .forEach(answer -> answer.setChecked(true));
    }
}
