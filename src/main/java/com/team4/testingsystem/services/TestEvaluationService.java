package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Test;

public interface TestEvaluationService {
    int getEvaluationBeforeCoachCheck(Test test);

    int getEvaluationAfterCoachCheck(Test test);
}
