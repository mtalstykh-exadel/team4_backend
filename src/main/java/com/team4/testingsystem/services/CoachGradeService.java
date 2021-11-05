package com.team4.testingsystem.services;

import com.team4.testingsystem.model.entity.CoachGrade;
import com.team4.testingsystem.model.entity.Test;

import java.util.Collection;

public interface CoachGradeService {
    Collection<CoachGrade> getGradesByTest(Test test);

    void add(Long testId, Long questionId, Integer grade, String comment);
}
