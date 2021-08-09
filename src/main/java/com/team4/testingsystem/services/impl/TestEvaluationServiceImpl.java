package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.exceptions.CoachGradeNotFoundException;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.services.ModuleGradesService;
import com.team4.testingsystem.services.TestEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestEvaluationServiceImpl implements TestEvaluationService {
    private final ChosenOptionService chosenOptionService;
    private final ModuleGradesService moduleGradesService;
    private final CoachGradeRepository coachGradeRepository;

    @Autowired
    public TestEvaluationServiceImpl(ChosenOptionService chosenOptionService,
                                     ModuleGradesService moduleGradesService,
                                     CoachGradeRepository coachGradeRepository) {
        this.chosenOptionService = chosenOptionService;
        this.moduleGradesService = moduleGradesService;
        this.coachGradeRepository = coachGradeRepository;
    }

    private void saveModuleGrade(Test test, String moduleName, Integer grade) {
        moduleGradesService.add(test, moduleName, grade);
    }

    private int countScoreAutomaticCheck(Test test, String moduleName, List<ChosenOption> allChosenOptions) {
        int score = (int) allChosenOptions
                .stream()
                .filter(chosenOption -> chosenOption.getId().getQuestion().getModule().getName().equals(moduleName))
                .map(ChosenOption::getAnswer)
                .filter(Answer::isCorrect)
                .count();

        saveModuleGrade(test, moduleName, score);

        return score;
    }


    private int getScoreCoachCheck(Test test, String moduleName, List<CoachGrade> allCoachGrades) {
        int score = allCoachGrades
                .stream()
                .filter(coachGrade -> coachGrade.getId().getQuestion().getModule().getName().equals(moduleName))
                .findAny()
                .orElseThrow(CoachGradeNotFoundException::new)
                .getGrade();

        saveModuleGrade(test, moduleName, score);

        return score;
    }

    @Override
    public int getEvaluationBeforeCoachCheck(Test test) {
        List<ChosenOption> allChosenOptions = chosenOptionService.getChosenOptionByTest(test);
        moduleGradesService.add(test, "Essay", 0);
        moduleGradesService.add(test, "Speaking", 0);

        return countScoreAutomaticCheck(test, "Grammar", allChosenOptions)
                + countScoreAutomaticCheck(test, "Listening", allChosenOptions);
    }

    @Override
    public int getEvaluationAfterCoachCheck(Test test) {
        List<CoachGrade> allCoachGrades = (List<CoachGrade>) coachGradeRepository.findAllById_Test(test);

        return test.getEvaluation()
                + getScoreCoachCheck(test, "Essay", allCoachGrades)
                + getScoreCoachCheck(test, "Speaking", allCoachGrades);
    }
}
