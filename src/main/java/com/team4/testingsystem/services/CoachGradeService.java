package com.team4.testingsystem.services;

import com.team4.testingsystem.dto.CoachGradeDTO;
import com.team4.testingsystem.entities.CoachGrade;

import java.util.Collection;

public interface CoachGradeService {
    CoachGrade getGrade(Long testId, Long questionId);

    Collection<CoachGrade> getGradesByTest(Long testId);

    void createGrade(CoachGradeDTO gradeDTO);

    void updateGrade(CoachGradeDTO gradeDTO);
}
