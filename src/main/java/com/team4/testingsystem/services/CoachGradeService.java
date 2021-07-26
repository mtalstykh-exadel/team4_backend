package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;

public interface CoachGradeService {
    CoachGrade getGrade(Long testId, Long questionId);

    Iterable<CoachGrade> getGradesByTest(Long testId);

    void createGrade(Test test, Question question, Integer grade);

    void updateGrade(Long testId, Long questionId, Integer grade);
}
