package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Test;

import java.util.Collection;

public interface CoachGradeService {
    Collection<CoachGrade> getGradesByTest(Test test);

    void add(Long testId, Long questionId, Integer grade, String comment);
}
