package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.CoachAnswerDTO;
import com.team4.testingsystem.entities.CoachAnswer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.repositories.CoachAnswerRepository;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.RestrictionsService;
import com.team4.testingsystem.services.TestsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CoachAnswerServiceImplTest {
    @Mock
    private CoachAnswerRepository coachAnswerRepository;

    @Mock
    private TestsService testsService;

    @Mock
    private QuestionService questionService;

    @Mock
    private TestQuestionID testQuestionID;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Mock
    private Question question;

    @Mock
    private List<CoachAnswer> coachAnswers;

    @Mock
    private RestrictionsService restrictionsService;

    @InjectMocks
    private CoachAnswerServiceImpl coachAnswerService;

    private final Long TEST_ID = 1L;
    private final Long QUESTION_ID = 1L;
    private final String COACH_COMMENT = "comment";

    @Test
    void getAnswersByTest() {
        Mockito.when(coachAnswerRepository.findAllByTestId(TEST_ID)).thenReturn(coachAnswers);

        Assertions.assertEquals(coachAnswers, coachAnswerService.getAnswersByTest(TEST_ID));
    }

    @Test
    void addAll() {
        try (MockedConstruction<CoachAnswer> mocked = Mockito.mockConstruction(CoachAnswer.class,
                (mock, context) -> {
                    Mockito.when(mock.getId()).thenReturn(testQuestionID);

                    Mockito.when(testQuestionID.getTest()).thenReturn(test);
                    Mockito.when(testQuestionID.getQuestion()).thenReturn(question);

                    List<CoachAnswerDTO> coachAnswersDTO = List.of(new CoachAnswerDTO(TEST_ID, QUESTION_ID, COACH_COMMENT));

                    coachAnswerService.addAll(coachAnswersDTO);

                    verify(coachAnswerRepository).saveAll(any());
                    verify(restrictionsService)
                            .checkTestContainsQuestion(test, question);
                    verify(restrictionsService).checkStatus(test, Status.STARTED);
                    verify(restrictionsService).checkCoachIsCurrentUser(test);
                }
        )){}
    }

}
