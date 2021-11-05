package com.team4.testingsystem.services;

import com.team4.testingsystem.model.entity.Test;

public interface TestEvaluationService {
    void countScoreBeforeCoachCheck(Test test);

    void updateScoreAfterCoachCheck(Test test);
}
