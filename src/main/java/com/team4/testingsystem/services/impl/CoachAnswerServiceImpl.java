package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.converters.CoachAnswerConverter;
import com.team4.testingsystem.dto.CoachAnswerDTO;
import com.team4.testingsystem.repositories.CoachAnswerRepository;
import com.team4.testingsystem.services.CoachAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoachAnswerServiceImpl implements CoachAnswerService {
    private final CoachAnswerRepository coachAnswerRepository;
    private final CoachAnswerConverter coachAnswerConverter;

    @Autowired
    public CoachAnswerServiceImpl(CoachAnswerRepository coachAnswerRepository,
                                  CoachAnswerConverter coachAnswerConverter) {
        this.coachAnswerRepository = coachAnswerRepository;
        this.coachAnswerConverter = coachAnswerConverter;
    }

    public void addAll(List<CoachAnswerDTO> coachAnswers) {
        coachAnswerRepository.saveAll(coachAnswers.stream()
                .map(coachAnswerConverter::convertToEntity)
                .collect(Collectors.toList()));
    }
}
