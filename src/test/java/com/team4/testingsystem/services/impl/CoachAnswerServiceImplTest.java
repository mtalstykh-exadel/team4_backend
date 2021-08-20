package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.CoachAnswer;
import com.team4.testingsystem.repositories.CoachAnswerRepository;
import com.team4.testingsystem.services.TestsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class CoachAnswerServiceImplTest {
    @Mock
    private CoachAnswerRepository coachAnswerRepository;

    @Mock
    private TestsService testsService;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Mock
    private List<CoachAnswer> coachAnswers;

    @InjectMocks
    private CoachAnswerServiceImpl coachAnswerService;

    private final Long TEST_ID = 1L;

    @Test
    void getAnswersByTest() {
        Mockito.when(test.getId()).thenReturn(TEST_ID);

        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);

        Mockito.when(coachAnswerRepository.findAllById_Test(test))
                .thenReturn(coachAnswers);

        Assertions.assertEquals(coachAnswers,
                coachAnswerService.getAnswersByTest(test.getId()));
    }
}