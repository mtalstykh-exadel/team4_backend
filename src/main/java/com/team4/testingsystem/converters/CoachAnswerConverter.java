package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.CoachAnswerDTO;
import com.team4.testingsystem.entities.CoachAnswer;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoachAnswerConverter {
    private final TestsService testsService;
    private final QuestionService questionService;

    @Autowired
    CoachAnswerConverter(TestsService testsService,
                         QuestionService questionService) {
        this.testsService = testsService;
        this.questionService = questionService;
    }

    public CoachAnswer convertToEntity(CoachAnswerDTO coachAnswerDTO) {
        return new CoachAnswer(new TestQuestionID(testsService.getById(coachAnswerDTO.getTestId()),
                questionService.getById(coachAnswerDTO.getQuestionId())),
                coachAnswerDTO.getComment());
    }
}
