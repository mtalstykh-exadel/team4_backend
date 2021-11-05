package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.GradesConverter;
import com.team4.testingsystem.converters.TestConverter;
import com.team4.testingsystem.converters.TestVerificationConverter;
import com.team4.testingsystem.model.dto.AssignTestRequest;
import com.team4.testingsystem.model.dto.ModuleGradesDTO;
import com.team4.testingsystem.model.dto.TestDTO;
import com.team4.testingsystem.model.dto.TestInfo;
import com.team4.testingsystem.model.dto.TestVerificationDTO;
import com.team4.testingsystem.model.entity.Test;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Priority;
import com.team4.testingsystem.exceptions.CoachAssignmentFailException;
import com.team4.testingsystem.exceptions.ModuleGradeNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.TestsLimitExceededException;
import com.team4.testingsystem.exceptions.UserNotFoundException;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.RestrictionsService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestsControllerTest {

    @Mock
    private ModuleGradesDTO moduleGradesDTO;

    @Mock
    private TestsService testsService;

    @Mock
    private CustomUserDetails customUserDetails;

    @Mock
    private GradesConverter gradesConverter;

    @Mock
    private TestConverter testConverter;

    @Mock
    private TestVerificationConverter verificationConverter;

    @Mock
    private RestrictionsService restrictionsService;

    @Mock
    private TestVerificationDTO testVerificationDTO;

    @InjectMocks
    private TestsController testsController;

    private Test test;
    private TestDTO testDTO;

    private static final Pageable PAGE_REQUEST = PageRequest.of(1, 10);
    private static final long GOOD_TEST_ID = 1L;
    private static final long BAD_TEST_ID = 42L;
    private static final long GOOD_USER_ID = 11L;
    private static final long BAD_USER_ID = 4242L;
    private static final int PAGE = 1;
    private static final int COUNT = 10;
    private static final Levels A1 = Levels.A1;
    private static final Priority LOW = Priority.LOW;
    private static final Priority MEDIUM = Priority.MEDIUM;
    private static final Instant TIME = Instant.now();

    @BeforeEach
    void init() {
        test = EntityCreatorUtil.createTest(EntityCreatorUtil.createUser(),
                EntityCreatorUtil.createLevel());
        testDTO = new TestDTO(test);
    }

    @org.junit.jupiter.api.Test
    void getByIdSuccess() {
            Mockito.when(testsService.getByIdWithRestrictions(GOOD_TEST_ID)).thenReturn(test);
            Mockito.when(testConverter.convertToDTO(test)).thenReturn(testDTO);

            Assertions.assertEquals(testDTO,
                    testsController.getById(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getByIdFail() {
        Mockito.when(testsService.getByIdWithRestrictions(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsController.getById(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getCurrentUserTestsEmpty() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            CustomUserDetails mockUserDetails = Mockito.mock(CustomUserDetails.class);
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(mockUserDetails);
            Mockito.when(mockUserDetails.getId()).thenReturn(GOOD_USER_ID);

            Mockito.when(testsService.getByUserId(GOOD_USER_ID, A1, PAGE_REQUEST)).thenReturn(Lists.list(test));
            TestInfo expectedInfo = new TestInfo(test, 4);
            Mockito.when(testConverter.convertToInfo(test)).thenReturn(expectedInfo);

            Assertions.assertEquals(Lists.list(expectedInfo),
                    testsController.getCurrentUserTests(A1, PAGE, COUNT));
        }
    }

    @org.junit.jupiter.api.Test
    void getGradesSuccess() {

            Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);
            Mockito.when(gradesConverter.convertListOfGradesToDTO(test)).thenReturn(moduleGradesDTO);

            testsController.getGrades(GOOD_TEST_ID);
            Assertions.assertEquals(moduleGradesDTO, testsController.getGrades(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getGradesFailTestNotFound() {
        Mockito.when(testsService.getById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsController.getGrades(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getGradesFailModuleGradeNotFound() {
        Mockito.when(testsService.getById(BAD_TEST_ID)).thenThrow(ModuleGradeNotFoundException.class);

        Assertions.assertThrows(ModuleGradeNotFoundException.class,
                () -> testsController.getGrades(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getUsersTestsSuccess() {
        Assertions.assertEquals(Lists.emptyList(),
                testsController.getUsersTests(GOOD_USER_ID, null, PAGE, COUNT));
    }

    @org.junit.jupiter.api.Test
    void getUsersTestsWithLevelSuccess() {
        List<Test> tests = new ArrayList<>();
        Mockito.when(testsService.getTestsByUserIdAndLevel(GOOD_USER_ID, A1, PAGE_REQUEST))
                .thenReturn(tests);

        Assertions.assertEquals(Lists.emptyList(),
                testsController.getUsersTests(GOOD_USER_ID, A1, PAGE, COUNT));
    }


    @org.junit.jupiter.api.Test
    void getUsersTestsWithLevelFailUserNotFound() {
        Mockito.when(testsService.getTestsByUserIdAndLevel(BAD_USER_ID, A1, PAGE_REQUEST))
                .thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> testsController.getUsersTests(BAD_USER_ID, A1, PAGE, COUNT));
    }

    @org.junit.jupiter.api.Test
    void getTestForVerificationNotFound() {
        Mockito.when(testsService.startTestVerification(BAD_TEST_ID))
                .thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsController.getTestForVerification(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getTestForVerificationSuccess() {
        Mockito.when(testsService.startTestVerification(GOOD_TEST_ID)).thenReturn(test);
        Mockito.when(verificationConverter.convertToVerificationDTO(test))
                .thenReturn(testVerificationDTO);

        Assertions.assertEquals(testVerificationDTO,
                testsController.getTestForVerification(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getUnverifiedTestsForCurrentCoachSuccess() {
        TestInfo testInfo = new TestInfo(test);
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(customUserDetails);
            Mockito.when(customUserDetails.getId()).thenReturn(GOOD_USER_ID);
            Mockito.when(testsService.getAllUnverifiedTestsByCoach(GOOD_USER_ID, PAGE_REQUEST))
                    .thenReturn(List.of(test));
            Mockito.when(testConverter.convertToInfo(test)).thenReturn(testInfo);

            Assertions.assertEquals(List.of(testInfo),
                    testsController.getUnverifiedTestsForCurrentCoach(PAGE, COUNT));
        }
    }

    @org.junit.jupiter.api.Test
    void assignSuccess() {

        AssignTestRequest request = new AssignTestRequest(A1, TIME, MEDIUM);
        Mockito.when(testsService.createAssigned(GOOD_USER_ID, A1, request.getDeadline(), MEDIUM))
                .thenReturn(GOOD_TEST_ID);


        Assertions.assertEquals(GOOD_TEST_ID, testsController.assign(GOOD_USER_ID, request));
    }

    @org.junit.jupiter.api.Test
    void assignFail() {

        AssignTestRequest request = new AssignTestRequest(A1, TIME, LOW);
        Mockito.when(testsService.createAssigned(BAD_USER_ID, A1, request.getDeadline(), LOW))

                .thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> testsController.assign(BAD_USER_ID, request));
    }

    @org.junit.jupiter.api.Test
    void deassignSuccess() {
        testsController.deassign(GOOD_TEST_ID);

        verify(testsService).deassign(GOOD_TEST_ID);
    }

    @org.junit.jupiter.api.Test
    void deassignFail() {
        doThrow(TestNotFoundException.class).when(testsService).deassign(BAD_TEST_ID);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsController.deassign(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void startNotAssignedSuccess() {
        TestDTO testDTO = new TestDTO(test);
        try (MockedStatic<JwtTokenUtil> builderMockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
            builderMockedStatic.when(JwtTokenUtil::extractUserDetails).thenReturn(customUserDetails);

            Mockito.when(customUserDetails.getId()).thenReturn(1L);
            Mockito.when(testsService.createNotAssigned(1L, Levels.A1)).thenReturn(1L);
            Mockito.when(testsService.startNotAssigned(1L)).thenReturn(test);

            Mockito.when(testConverter.convertToDTO(test)).thenReturn(testDTO);

            Assertions.assertEquals(testDTO, testsController.startNotAssigned(A1));
        }
    }

    @org.junit.jupiter.api.Test
    void startNotAssignedFail() {
        try (MockedStatic<JwtTokenUtil> builderMockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
            builderMockedStatic.when(JwtTokenUtil::extractUserDetails).thenReturn(customUserDetails);

            Mockito.when(customUserDetails.getId()).thenReturn(1L);
            Mockito.when(testsService.createNotAssigned(1L, Levels.A1))
                    .thenThrow(TestsLimitExceededException.class);


            Assertions.assertThrows(TestsLimitExceededException.class,
                    () -> testsController.startNotAssigned(A1));
        }
    }

    @org.junit.jupiter.api.Test
    void startAssignedSuccess() {
        Mockito.when(testsService.startAssigned(GOOD_TEST_ID))
                .thenReturn(test);
        Mockito.when(testConverter.convertToDTO(test)).thenReturn(testDTO);

        testsController.startAssigned(GOOD_TEST_ID);

        verify(testsService).startAssigned(GOOD_TEST_ID);

        Assertions.assertEquals(testDTO, testsController.startAssigned(GOOD_TEST_ID));

    }

    @org.junit.jupiter.api.Test
    void startAssignedFail() {

        doThrow(TestNotFoundException.class).when(testsService).startAssigned(BAD_TEST_ID);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsController.startAssigned(BAD_TEST_ID));

    }

    @org.junit.jupiter.api.Test
    void finishSuccess() {
        testsController.finish(GOOD_TEST_ID);
        verify(testsService).selfFinish(GOOD_TEST_ID);
    }


    @org.junit.jupiter.api.Test
    void finishFail() {

            doThrow(TestNotFoundException.class).when(testsService).selfFinish(anyLong());

            Assertions.assertThrows(TestNotFoundException.class,
                    () -> testsController.finish(BAD_TEST_ID));

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

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsController.deassignCoach(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getUnverifiedTests() {
        TestInfo testInfo = new TestInfo(test);
        Mockito.when(testsService.getAllUnverifiedTests(PAGE_REQUEST)).thenReturn(List.of(test));
        Mockito.when(testConverter.convertToInfo(test)).thenReturn(testInfo);
        Assertions.assertEquals(List.of(testInfo), testsController.getUnverifiedTests(PAGE, COUNT));
    }

    @org.junit.jupiter.api.Test
    void spendAttempt(){
        testsController.spendAttempt(GOOD_TEST_ID);

        verify(testsService).spendAttempt(GOOD_TEST_ID);
    }

}
