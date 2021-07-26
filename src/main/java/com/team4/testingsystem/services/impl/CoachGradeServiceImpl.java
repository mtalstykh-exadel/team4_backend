package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.exceptions.GradeNotFoundException;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.CoachGradeService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoachGradeServiceImpl implements CoachGradeService {
    private final CoachGradeRepository gradeRepository;

    private final TestsService testsService;
    private final QuestionService questionService;

    @Autowired
    public CoachGradeServiceImpl(CoachGradeRepository gradeRepository,
                                 TestsService testsService,
                                 QuestionService questionService) {
        this.gradeRepository = gradeRepository;
        this.testsService = testsService;
        this.questionService = questionService;
    }

    @Override
    public CoachGrade getGrade(Long testId, Long questionId) {
        return gradeRepository.findByTestAndQuestion(
                testsService.getById(testId),
                questionService.getQuestionById(questionId)
        ).orElseThrow(GradeNotFoundException::new);
    }

    @Override
    public Iterable<CoachGrade> getGradesByTest(Long testId) {
        return gradeRepository.findAllByTest(testsService.getById(testId));
    }

    @Override
    public void createGrade(Test test, Question question, Integer grade) {
        gradeRepository.save(new CoachGrade(test, question, grade));
    }

    @Override
    public void updateGrade(Long testId, Long questionId, Integer grade) {
        if (gradeRepository.updateGrade(testId, questionId, grade) == 0) {
            throw new GradeNotFoundException();
        }
    }
}
