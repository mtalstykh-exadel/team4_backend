package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.CoachAnswer;
import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.CoachGradeNotFoundException;
import com.team4.testingsystem.repositories.CoachAnswerRepository;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.services.ModuleCoachAnswersService;
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
    private final ModuleCoachAnswersService moduleCoachAnswersService;
    private final CoachGradeRepository coachGradeRepository;
    private final CoachAnswerRepository coachAnswerRepository;

    @Autowired
    public TestEvaluationServiceImpl(ChosenOptionService chosenOptionService,
                                     ModuleGradesService moduleGradesService,
                                     ModuleCoachAnswersService moduleCoachAnswersService,
                                     CoachGradeRepository coachGradeRepository,
                                     CoachAnswerRepository coachAnswerRepository) {
        this.chosenOptionService = chosenOptionService;
        this.moduleGradesService = moduleGradesService;
        this.moduleCoachAnswersService = moduleCoachAnswersService;
        this.coachGradeRepository = coachGradeRepository;
        this.coachAnswerRepository = coachAnswerRepository;
    }

    private void saveModuleGrade(Test test, String moduleName, Integer grade, String coachComment) {
        moduleGradesService.add(test, moduleName, grade, coachComment);
    }

    private void saveModuleCoachAnswer(String moduleName, List<CoachAnswer> coachAnswers) {
        moduleCoachAnswersService.add(moduleName, coachAnswers);
    }

    private void saveScoreAutomaticCheck(Test test, Modules module, List<ChosenOption> allChosenOptions) {
        int score = (int) allChosenOptions
                .stream()
                .filter(chosenOption ->
                        chosenOption.getId().getQuestion().getModule().getName().equals(module.getName()))
                .map(ChosenOption::getAnswer)
                .filter(Answer::isCorrect)
                .count();
        saveModuleGrade(test, module.getName(), score, null);

    }

    private void saveAnswersCoachCheck(Modules module, List<CoachAnswer> allCoachAnswers) {
        List<CoachAnswer> coachAnswers = allCoachAnswers.stream()
                .filter(coachAnswer ->
                        coachAnswer.getId().getQuestion().getModule().getName().equals(module.getName()))
                .collect(Collectors.toList());

        if (!coachAnswers.isEmpty()) {
            saveModuleCoachAnswer(module.getName(), coachAnswers);
        }
    }

    private void saveScoreCoachCheck(Test test, Modules module, Map<String, CoachGrade> gradeMap) {
        CoachGrade grade = Optional.ofNullable(gradeMap.get(module.getName()))
                .orElseThrow(CoachGradeNotFoundException::new);

        saveModuleGrade(test, module.getName(), grade.getGrade(), grade.getComment());
    }

    @Override
    public void countScoreBeforeCoachCheck(Test test) {
        List<ChosenOption> chosenOptions = chosenOptionService.getAllByTestId(test.getId());

        moduleGradesService.add(test, Modules.ESSAY.getName(), 0, null);
        moduleGradesService.add(test, Modules.SPEAKING.getName(), 0, null);

        saveScoreAutomaticCheck(test, Modules.GRAMMAR, chosenOptions);
        saveScoreAutomaticCheck(test, Modules.LISTENING, chosenOptions);
    }

    @Override
    public void updateScoreAfterCoachCheck(Test test) {
        List<CoachGrade> allCoachGrades = (List<CoachGrade>) coachGradeRepository.findAllById_Test(test);

        Map<String, CoachGrade> gradeMap = allCoachGrades
            .stream()
            .collect(Collectors.toMap(
                coachGrade -> coachGrade.getId().getQuestion().getModule().getName(),
                coachGrade -> coachGrade));

        List<CoachAnswer> allCoachAnswers = coachAnswerRepository.findAllById_Test(test);

        saveAnswersCoachCheck(Modules.GRAMMAR, allCoachAnswers);
        saveAnswersCoachCheck(Modules.LISTENING, allCoachAnswers);
        saveScoreCoachCheck(test, Modules.ESSAY, gradeMap);
        saveScoreCoachCheck(test, Modules.SPEAKING, gradeMap);
    }
}
