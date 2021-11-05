package com.team4.testingsystem.services;

import com.team4.testingsystem.model.dto.CoachAnswerDTO;
import com.team4.testingsystem.model.entity.CoachAnswer;

import java.util.List;

public interface CoachAnswerService {
    void addAll(List<CoachAnswerDTO> coachAnswers);

    List<CoachAnswer> getAnswersByTest(Long testId);
}
