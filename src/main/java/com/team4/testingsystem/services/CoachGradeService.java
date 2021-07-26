package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;

public interface CoachGradeService {
    CoachGrade getGradeById(Long id);

    CoachGrade getGradeByQuestion(Question question);

    void createGrade(Question question, Test test, Integer grade);

    void updateGrade(Long id, Integer grade);
}
