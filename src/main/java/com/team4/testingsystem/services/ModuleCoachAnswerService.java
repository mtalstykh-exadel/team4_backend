package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.CoachAnswer;

import java.util.List;

public interface ModuleCoachAnswerService {
    void addAnswers(String moduleName, List<CoachAnswer> coachAnswers);
}
