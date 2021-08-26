package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.ChosenOptionBadRequestException;
import com.team4.testingsystem.exceptions.ChosenOptionNotFoundException;
import com.team4.testingsystem.repositories.ChosenOptionRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.RestrictionsService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ChosenOptionServiceImplTest {

    @Mock
    private ChosenOption chosenOption;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Mock
    private CustomUserDetails userDetails;

    @Mock
    private RestrictionsService restrictionsService;

    @Mock
    private ChosenOptionRepository chosenOptionRepository;

    @Mock
    private List<ChosenOption> chosenOptions;

    @InjectMocks
    private ChosenOptionServiceImpl chosenOptionService;

    private static final Long TEST_ID = 1L;
    private static final Long QUESTION_ID = 2L;
    private static final Long USER_ID = 2L;

    @Test
    void getByIdSuccess() {
        Mockito.when(chosenOptionRepository.findByTestAndQuestionId(TEST_ID, QUESTION_ID))
                .thenReturn(Optional.of(chosenOption));

        Assertions.assertEquals(chosenOption, chosenOptionService.getByTestAndQuestionId(TEST_ID, QUESTION_ID));
    }

    @Test
    void getByIdFail() {
        Mockito.when(chosenOptionRepository.findByTestAndQuestionId(TEST_ID, QUESTION_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ChosenOptionNotFoundException.class,
                () -> chosenOptionService.getByTestAndQuestionId(TEST_ID, QUESTION_ID));
    }

    @Test
    void getChosenOptionByTestSuccess() {
        List<ChosenOption> chosenOptions = List.of(new ChosenOption(), new ChosenOption());
        Mockito.when(chosenOptionRepository.findByTestId(TEST_ID)).thenReturn(chosenOptions);

        Assertions.assertEquals(chosenOptions, chosenOptionService.getAllByTestId(TEST_ID));
    }

    @Test
    void getEmptyChosenOptionByTest() {
        List<ChosenOption> chosenOptions = List.of();
        Mockito.when(chosenOptionRepository.findByTestId(TEST_ID)).thenReturn(chosenOptions);

        Assertions.assertEquals(chosenOptions, chosenOptionService.getAllByTestId(TEST_ID));
    }

    @Test
    void saveAllSuccess() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(QUESTION_ID);
            chosenOptions = List.of(EntityCreatorUtil.createChosenOption());
            chosenOptionService.saveAll(chosenOptions);
            test = chosenOptions.get(0).getId().getTest();
            Question question = chosenOptions.get(0).getId().getQuestion();

            Mockito.verify(restrictionsService).checkOwnerIsCurrentUser(test, USER_ID);
            Mockito.verify(restrictionsService).checkStatus(test, Status.STARTED);
            Mockito.verify(restrictionsService).checkTestContainsQuestion(test, question);
            Mockito.verify(chosenOptionRepository).saveAll(chosenOptions);
        }
    }

    @Test
    void saveAllEntityNotFound() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(QUESTION_ID);
            chosenOptions = List.of(EntityCreatorUtil.createChosenOption());
            Mockito.when(chosenOptionRepository.saveAll(chosenOptions))
                    .thenThrow(EntityNotFoundException.class);
            Assertions.assertThrows(ChosenOptionBadRequestException.class,
                    () -> chosenOptionService.saveAll(chosenOptions));
        }
    }
}
