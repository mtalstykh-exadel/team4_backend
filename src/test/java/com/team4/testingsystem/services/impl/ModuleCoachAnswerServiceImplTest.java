package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.*;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.repositories.ModuleCoachAnswerRepository;
import com.team4.testingsystem.services.ModuleService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class ModuleCoachAnswerServiceImplTest {
    @Mock
    private ModuleService moduleService;

    @Mock
    private ModuleCoachAnswerRepository moduleCoachAnswerRepository;

    @Mock
    private CoachAnswer coachAnswer;

    @Mock
    private List<ModuleCoachAnswer> moduleCoachAnswers;

    @Mock
    private Stream<CoachAnswer> coachAnswerStream;

    @Mock
    private Module module;

    @Mock
    private Level level;

    @Mock
    private User user;

    private String COACH_COMMENT = "comment";

    @InjectMocks
    private ModuleCoachAnswerServiceImpl moduleCoachAnswerService;

    @org.junit.jupiter.api.Test
    public void addAnswers()  {

        Mockito.when(moduleService.getModuleByName(module.getName())).thenReturn(module);

        coachAnswer = new CoachAnswer(new TestQuestionID(EntityCreatorUtil.createTest(user, level),
                EntityCreatorUtil.createQuestion()), COACH_COMMENT);

        Assertions.assertDoesNotThrow(() -> moduleCoachAnswerService.addAnswers(module.getName(), List.of(coachAnswer)));

        Mockito.verify(moduleCoachAnswerRepository).saveAll(Mockito.any());
    }
}