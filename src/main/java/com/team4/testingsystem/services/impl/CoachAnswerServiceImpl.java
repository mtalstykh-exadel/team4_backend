package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.CoachAnswerDTO;
import com.team4.testingsystem.entities.CoachAnswer;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.repositories.CoachAnswerRepository;
import com.team4.testingsystem.services.CoachAnswerService;
import com.team4.testingsystem.services.QuestionService;
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

    @Autowired
    public CoachAnswerServiceImpl(CoachAnswerRepository coachAnswerRepository,
                                  TestsService testsService,
                                  QuestionService questionService) {
        this.coachAnswerRepository = coachAnswerRepository;
        this.testsService = testsService;
        this.questionService = questionService;
    }

    @Override
    public void addAll(List<CoachAnswerDTO> coachAnswers) {
        coachAnswerRepository.saveAll(coachAnswers.stream()
                .map(this::convertToEntity)
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
}
