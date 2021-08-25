package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.CoachAnswerDTO;
import com.team4.testingsystem.entities.CoachAnswer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.repositories.CoachAnswerRepository;
import com.team4.testingsystem.services.CoachAnswerService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.RestrictionsService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoachAnswerServiceImpl implements CoachAnswerService {
    private final CoachAnswerRepository coachAnswerRepository;
    private final TestsService testsService;
    private final QuestionService questionService;
    private final RestrictionsService restrictionsService;

    @Autowired
    public CoachAnswerServiceImpl(CoachAnswerRepository coachAnswerRepository,
                                  TestsService testsService,
                                  QuestionService questionService,
                                  RestrictionsService restrictionsService) {
        this.coachAnswerRepository = coachAnswerRepository;
        this.testsService = testsService;
        this.questionService = questionService;
        this.restrictionsService = restrictionsService;
    }

    @Override
    public void addAll(List<CoachAnswerDTO> coachAnswers) {
        coachAnswerRepository.saveAll(coachAnswers.stream()
                .map(this::convertToEntity)
                .peek(this::addCommentRestrictions)
                .collect(Collectors.toList()));
    }

    @Override
    public List<CoachAnswer> getAnswersByTest(Long testId) {
        return coachAnswerRepository.findAllById_Test(testsService.getById(testId));
    }

    private CoachAnswer convertToEntity(CoachAnswerDTO coachAnswerDTO) {
        return new CoachAnswer(
                new TestQuestionID(testsService.getById(coachAnswerDTO.getTestId()),
                        questionService.getById(coachAnswerDTO.getQuestionId())),
                coachAnswerDTO.getComment());
    }

    private void addCommentRestrictions(CoachAnswer coachAnswer) {
        Test test = coachAnswer.getId().getTest();
        Question question = coachAnswer.getId().getQuestion();

        restrictionsService.checkCoachIsCurrentUser(test);
        restrictionsService.checkStatus(test, Status.IN_VERIFICATION);
        restrictionsService.checkTestContainsQuestion(test, question);
    }
}
