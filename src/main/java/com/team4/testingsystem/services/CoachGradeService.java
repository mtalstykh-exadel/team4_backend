package com.team4.testingsystem.services;

import com.team4.testingsystem.dto.CoachGradeRequest;
import com.team4.testingsystem.entities.CoachGrade;

public interface CoachGradeService {
    CoachGrade getGrade(Long testId, Long questionId);

    Iterable<CoachGrade> getGradesByTest(Long testId);

    void createGrade(CoachGradeRequest gradeRequest);

    void updateGrade(CoachGradeRequest gradeRequest);
}
