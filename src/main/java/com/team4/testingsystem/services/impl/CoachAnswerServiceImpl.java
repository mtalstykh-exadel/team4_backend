package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.CoachAnswer;
import com.team4.testingsystem.repositories.CoachAnswerRepository;
import com.team4.testingsystem.services.CoachAnswerService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachAnswerServiceImpl implements CoachAnswerService {
    private final CoachAnswerRepository coachAnswerRepository;
    private final TestsService testsService;

    @Autowired
    public CoachAnswerServiceImpl(CoachAnswerRepository coachAnswerRepository,
                                  TestsService testsService) {
        this.coachAnswerRepository = coachAnswerRepository;
        this.testsService = testsService;
    }

    @Override
    public List<CoachAnswer> getAnswersByTest(Long testId) {
        return coachAnswerRepository.findAllById_Test(testsService.getById(testId));
    }
}
