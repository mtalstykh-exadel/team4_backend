package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.exceptions.CoachGradeNotFoundException;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.services.ModuleGradesService;
import com.team4.testingsystem.services.TestEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private void saveModuleGrade(Test test, String moduleName, Integer grade){
        moduleGradesService.add(test, moduleName, grade);
    }

    private int countGrammarScore(Test test) {
        int grammarScore = (int) chosenOptionService.getChosenOptionByTest(test)
                .stream()
                .filter(chosenOption -> chosenOption.getId().getQuestion().getModule().getName().equals("Grammar"))
                .map(ChosenOption::getAnswer)
                .filter(Answer::isCorrect)
                .count();

        saveModuleGrade(test, "Grammar", grammarScore);

        return grammarScore;
    }

    private int countListeningScore(Test test) {
        int listeningScore = (int) chosenOptionService.getChosenOptionByTest(test)
                .stream()
                .filter(chosenOption -> chosenOption.getId().getQuestion().getModule().getName().equals("Listening"))
                .map(ChosenOption::getAnswer)
                .filter(Answer::isCorrect)
                .count();

        saveModuleGrade(test, "Listening", listeningScore);

        return listeningScore;
    }

    private int getEssayScore(Test test) {
        int essayScore = coachGradeRepository.findAllById_Test(test)
                .stream()
                .filter(coachGrade -> coachGrade.getId().getQuestion().getModule().getName().equals("Essay"))
                .findAny()
                .orElseThrow(CoachGradeNotFoundException::new)
                .getGrade();

        saveModuleGrade(test, "Essay", essayScore);

        return essayScore;
    }

    private int getSpeakingScore(Test test) {
        int speakingScore = coachGradeRepository.findAllById_Test(test)
                .stream()
                .filter(coachGrade -> coachGrade.getId().getQuestion().getModule().getName().equals("Speaking"))
                .findAny()
                .orElseThrow(CoachGradeNotFoundException::new)
                .getGrade();

        saveModuleGrade(test, "Speaking", speakingScore);

        return speakingScore;
    }

    @Override
    public int getEvaluationBeforeCoachCheck(Test test) {
        moduleGradesService.add(test, "Essay", 0 );
        moduleGradesService.add(test, "Speaking", 0);
        return countGrammarScore(test) + countListeningScore(test);
    }

    @Override
    public int getEvaluationAfterCoachCheck(Test test) {
        return test.getEvaluation() + getEssayScore(test) + getSpeakingScore(test);
    }
}
