package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.TestConverter;
import com.team4.testingsystem.dto.AssignTestRequest;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.exceptions.CoachAssignmentFailException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.UserNotFoundException;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.TestGeneratingService;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestsControllerTest {

    private final long GOOD_TEST_ID = 1L;
    private final long BAD_TEST_ID = 42L;

    private final long GOOD_USER_ID = 11L;
    private final long BAD_USER_ID = 4242L;

    @Mock
    private TestsService testsService;

    @Mock
    private CustomUserDetails customUserDetails;

    @Mock
    private TestGeneratingService testGeneratingService;

    @Mock
    private TestConverter testConverter;

    @InjectMocks
    private TestsController testsController;

    private Test test;

    @BeforeEach
    void init() {
        test = EntityCreatorUtil.createTest(EntityCreatorUtil.createUser());
    }

    @org.junit.jupiter.api.Test
    void getByIdSuccess() {
        Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);

        Assertions.assertEquals(new TestDTO(test), testsController.getById(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getByIdFail() {
        Mockito.when(testsService.getById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsController.getById(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getCurrentUserTestsEmpty() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            CustomUserDetails mockUserDetails = Mockito.mock(CustomUserDetails.class);
            Mockito.when(mockUserDetails.getId()).thenReturn(1L);

            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(mockUserDetails);

            Mockito.when(testsService.getByUserId(1L)).thenReturn(Lists.list(test));

            Assertions.assertEquals(Lists.list(new TestDTO(test)), testsController.getCurrentUserTests());
        }
    }

    @org.junit.jupiter.api.Test
    void getUsersTestsSuccess() {
        List<Test> tests = new ArrayList<>();

        Mockito.when(testsService.getByUserId(GOOD_USER_ID)).thenReturn(tests);

        Assertions.assertEquals(Lists.emptyList(), testsController.getUsersTests(GOOD_USER_ID));
    }


    @org.junit.jupiter.api.Test
    void getUsersTestsFailUserNotFound() {
        Mockito.when(testsService.getByUserId(BAD_USER_ID)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> testsController.getUsersTests(BAD_USER_ID));
    }

    @org.junit.jupiter.api.Test
    void assignSuccess() {
        AssignTestRequest request = new AssignTestRequest(Levels.A1, LocalDateTime.now());

        Mockito.when(testsService.assignForUser(GOOD_USER_ID, Levels.A1, request.getDeadline()))
                .thenReturn(1L);

        Assertions.assertEquals(1L, testsController.assign(GOOD_USER_ID, request));
    }

    @org.junit.jupiter.api.Test
    void assignFail() {
        AssignTestRequest request = new AssignTestRequest(Levels.A1, LocalDateTime.now());

        Mockito.when(testsService.assignForUser(BAD_USER_ID, Levels.A1, request.getDeadline()))
                .thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> testsController.assign(BAD_USER_ID, request));
    }

    @org.junit.jupiter.api.Test
    void startNotAssignedSuccess() {
        Test test = EntityCreatorUtil.createTest(new User());
        TestDTO testDTO = new TestDTO(test);
        try (MockedStatic<JwtTokenUtil> builderMockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
            builderMockedStatic.when(JwtTokenUtil::extractUserDetails).thenReturn(customUserDetails);
            Mockito.when(customUserDetails.getId()).thenReturn(1L);
            Mockito.when(testsService.startForUser(1L, Levels.A1)).thenReturn(1L);
            Mockito.when(testsService.start(1L)).thenReturn(testDTO);

            Assertions.assertEquals(testDTO, testsController.startNotAssigned(Levels.A1));
        }
    }

    @org.junit.jupiter.api.Test
    void startAssignedSuccess() {
        testsController.startAssigned(GOOD_TEST_ID);

        verify(testsService).start(GOOD_TEST_ID);
    }

    @org.junit.jupiter.api.Test
    void startAssignedFail() {
        doThrow(TestNotFoundException.class).when(testsService).start(BAD_TEST_ID);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsController.startAssigned(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void finishSuccess() {
        testsController.finish(GOOD_TEST_ID, 1);

        verify(testsService).finish(GOOD_TEST_ID, 1);
    }

    @org.junit.jupiter.api.Test
    void finishFail() {
        doThrow(TestNotFoundException.class).when(testsService).finish(BAD_TEST_ID, 42);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsController.finish(BAD_TEST_ID, 42));
    }

    @org.junit.jupiter.api.Test
    void updateEvaluationSuccess() {
        testsController.updateEvaluation(GOOD_TEST_ID, 1);

        verify(testsService).updateEvaluation(GOOD_TEST_ID, 1);
    }

    @org.junit.jupiter.api.Test
    void updateEvaluationFail() {
        doThrow(TestNotFoundException.class).when(testsService).updateEvaluation(BAD_TEST_ID, 42);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsController.updateEvaluation(BAD_TEST_ID, 42));
    }


    @org.junit.jupiter.api.Test
    void assignCoachSuccess() {
        testsController.assignCoach(GOOD_TEST_ID, GOOD_USER_ID);

        verify(testsService).assignCoach(GOOD_TEST_ID, GOOD_USER_ID);
    }

    @org.junit.jupiter.api.Test
    void assignCoachFailUserNotFound() {
        doThrow(UserNotFoundException.class).when(testsService).assignCoach(GOOD_TEST_ID, BAD_USER_ID);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> testsController.assignCoach(GOOD_TEST_ID, BAD_USER_ID));
    }

    @org.junit.jupiter.api.Test
    void assignCoachFailTestNotFound() {
        doThrow(TestNotFoundException.class).when(testsService).assignCoach(BAD_TEST_ID, GOOD_USER_ID);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsController.assignCoach(BAD_TEST_ID, GOOD_USER_ID));
    }

    @org.junit.jupiter.api.Test
    void assignCoachFailSelfAssignment() {
        doThrow(CoachAssignmentFailException.class).when(testsService).assignCoach(GOOD_TEST_ID, GOOD_USER_ID);

        Assertions.assertThrows(CoachAssignmentFailException.class,
                () -> testsController.assignCoach(GOOD_TEST_ID, GOOD_USER_ID));
    }


    @org.junit.jupiter.api.Test
    void deassignCoachSuccess() {
        testsController.deassignCoach(GOOD_TEST_ID);

        verify(testsService).deassignCoach(GOOD_TEST_ID);
    }

    @org.junit.jupiter.api.Test
    void deassignCoachFail() {
        doThrow(TestNotFoundException.class).when(testsService).deassignCoach(BAD_TEST_ID);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsController.deassignCoach(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getUnverifiedTests() {
        List<Test> tests = new ArrayList<>();
        List<TestDTO> testsDto = new ArrayList<>();
        Mockito.when(testsService.getByStatuses(any())).thenReturn(tests);
        Assertions.assertEquals(testsDto, testsController.getUnverifiedTests());
    }
}
