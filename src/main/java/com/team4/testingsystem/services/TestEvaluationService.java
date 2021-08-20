package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Test;

public interface TestEvaluationService {
    void countScoreBeforeCoachCheck(Test test);

    void updateScoreAfterCoachCheck(Test test);

    void updateCoachAnswersAfterCheck(Test test);
}
