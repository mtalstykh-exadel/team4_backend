package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.CoachGrade;

import java.util.Collection;

public interface CoachGradeService {
    Collection<CoachGrade> getGradesByTest(Long testId);

    void add(Long testId, Long questionId, Integer grade);

}
