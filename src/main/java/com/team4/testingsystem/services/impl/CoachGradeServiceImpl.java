package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.exceptions.GradeNotFoundException;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.CoachGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoachGradeServiceImpl implements CoachGradeService {
    private final CoachGradeRepository gradeRepository;

    @Autowired
    public CoachGradeServiceImpl(CoachGradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public CoachGrade getGradeById(Long id) {
        return gradeRepository.findById(id).orElseThrow(GradeNotFoundException::new);
    }

    @Override
    public CoachGrade getGradeByQuestion(Question question) {
        return gradeRepository.findByQuestion(question).orElseThrow(GradeNotFoundException::new);
    }

    @Override
    public void createGrade(Question question, Test test, Integer grade) {
        gradeRepository.save(new CoachGrade(grade, question, test));
    }

    @Override
    public void updateGrade(Long id, Integer grade) {
        if (gradeRepository.updateGrade(id, grade) == 0) {
            throw new GradeNotFoundException();
        }
    }
}
