package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.CoachGradeNotFoundException;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.services.ModuleGradesService;
import com.team4.testingsystem.services.TestEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private void saveScoreAutomaticCheck(Test test, Modules module, List<ChosenOption> allChosenOptions) {
        int score = (int) allChosenOptions
                .stream()
                .filter(chosenOption ->
                        chosenOption.getId().getQuestion().getModule().getName().equals(module.getName()))
                .map(ChosenOption::getAnswer)
                .filter(Answer::isCorrect)
                .count();
        saveModuleGrade(test, module.getName(), score);

    }


    private void saveScoreCoachCheck(Test test, Modules module, Map<String, Integer> gradeMap) {
        int score = Optional.ofNullable(gradeMap.get(module.getName()))
                .orElseThrow(() -> new CoachGradeNotFoundException());
        saveModuleGrade(test, module.getName(), score);
    }

    @Override
    public void countScoreBeforeCoachCheck(Test test) {
        List<ChosenOption> chosenOptions = chosenOptionService.getChosenOptionByTest(test);

        moduleGradesService.add(test, Modules.ESSAY.getName(), 0);
        moduleGradesService.add(test, Modules.SPEAKING.getName(), 0);

        saveScoreAutomaticCheck(test, Modules.GRAMMAR, chosenOptions);
        saveScoreAutomaticCheck(test, Modules.LISTENING, chosenOptions);
    }

    @Override
    public void updateScoreAfterCoachCheck(Test test) {
        List<CoachGrade> allCoachGrades = (List<CoachGrade>) coachGradeRepository.findAllById_Test(test);

        Map<String, Integer> gradeMap = allCoachGrades
                .stream()
                .collect(Collectors.toMap(coachGrade -> coachGrade.getId().getQuestion().getModule().getName(),
                        CoachGrade::getGrade));

        saveScoreCoachCheck(test, Modules.ESSAY, gradeMap);
        saveScoreCoachCheck(test, Modules.SPEAKING, gradeMap);
    }
}
