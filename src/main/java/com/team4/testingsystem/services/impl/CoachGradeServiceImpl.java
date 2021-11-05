package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.model.entity.CoachGrade;
import com.team4.testingsystem.model.entity.Question;
import com.team4.testingsystem.model.entity.Test;
import com.team4.testingsystem.model.entity.TestQuestionID;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.CoachGradeService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.RestrictionsService;
import com.team4.testingsystem.services.TestsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CoachGradeServiceImpl implements CoachGradeService {
    private final CoachGradeRepository gradeRepository;

    private final TestsService testsService;
    private final QuestionService questionService;
    private final RestrictionsService restrictionsService;

    @Override
    public Collection<CoachGrade> getGradesByTest(Test test) {
        restrictionsService.checkStatus(test, Status.IN_VERIFICATION);

        restrictionsService.checkCoachIsCurrentUser(test);

        return gradeRepository.findAllById_Test(test);
    }

    @Override
    public void add(Long testId, Long questionId, Integer grade, String comment) {

        Test test = testsService.getById(testId);

        Question question = questionService.getById(questionId);

        restrictionsService.checkCoachIsCurrentUser(test);

        restrictionsService.checkStatus(test, Status.IN_VERIFICATION);

        restrictionsService.checkTestContainsQuestion(test, question);

        restrictionsService.checkModuleIsEssayOrSpeaking(question);

        restrictionsService.checkGradeIsCorrect(grade);

        TestQuestionID testQuestionID = new TestQuestionID(test, question);

        gradeRepository.save(new CoachGrade(testQuestionID, grade, comment));

    }

}
