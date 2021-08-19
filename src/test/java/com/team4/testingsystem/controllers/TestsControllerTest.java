package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.GradesConverter;
import com.team4.testingsystem.converters.TestConverter;
import com.team4.testingsystem.converters.TestVerificationConverter;
import com.team4.testingsystem.dto.AssignTestRequest;
import com.team4.testingsystem.dto.ModuleGradesDTO;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.dto.TestInfo;
import com.team4.testingsystem.dto.TestVerificationDTO;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.ModuleGrade;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Priority;
import com.team4.testingsystem.exceptions.CoachAssignmentFailException;
import com.team4.testingsystem.exceptions.ModuleGradeNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.TestsLimitExceededException;
import com.team4.testingsystem.exceptions.UserNotFoundException;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.ModuleGradesService;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestsControllerTest {

    private final long GOOD_TEST_ID = 1L;
    private final long BAD_TEST_ID = 42L;

    private final long GOOD_USER_ID = 11L;
    private final long BAD_USER_ID = 4242L;

    @Mock
    private ModuleGradesDTO moduleGradesDTO;

    @Mock
    private ModuleGradesService moduleGradesService;

    @Mock
    private TestsService testsService;

    @Mock
    private CustomUserDetails customUserDetails;

    @Mock
    private GradesConverter gradesConverter;

    @Mock
    private Map<String, ModuleGrade> gradesMap;

    @Mock
    private TestConverter testConverter;

    @Mock
    private TestVerificationConverter verificationConverter;

    @Mock
    private RestrictionsService restrictionsService;

    @InjectMocks
    private TestsController testsController;

    private Test test;

    private final Pageable pageable = PageRequest.of(1, 10);

    @Mock
    private TestVerificationDTO testVerificationDTO;

    @BeforeEach
    void init() {
        test = EntityCreatorUtil.createTest(EntityCreatorUtil.createUser(), EntityCreatorUtil.createLevel());
    }

    @org.junit.jupiter.api.Test
    void getByIdSuccess() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(customUserDetails);
            Mockito.when(customUserDetails.getId()).thenReturn(GOOD_USER_ID);

            Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);

            Assertions.assertEquals(testConverter.convertToDTO(test), testsController.getById(GOOD_TEST_ID));
        }
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
            Test test = EntityCreatorUtil
                    .createTest(EntityCreatorUtil.createUser(), EntityCreatorUtil.createLevel());
            Mockito.when(mockUserDetails.getId()).thenReturn(1L);
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(mockUserDetails);
            Mockito.when(testsService.getByUserId(1L, pageable)).thenReturn(Lists.list(test));
            TestInfo expectedInfo = new TestInfo(test, 4);
            Mockito.when(testConverter.convertToInfo(test)).thenReturn(expectedInfo);

            Assertions.assertEquals(Lists.list(expectedInfo),
                    testsController.getCurrentUserTests(1, 10));
        }
    }

    @org.junit.jupiter.api.Test
    void getGradesSuccess() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(customUserDetails);
            Mockito.when(customUserDetails.getId()).thenReturn(GOOD_USER_ID);

            Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);

            Mockito.when(moduleGradesService.getGradesByTest(test)).thenReturn(gradesMap);

            Mockito.when(gradesConverter.convertListOfGradesToDTO(gradesMap)).thenReturn(moduleGradesDTO);

            Assertions.assertEquals(moduleGradesDTO, testsController.getGrades(GOOD_TEST_ID));
        }
    }

    @org.junit.jupiter.api.Test
    void getGradesFailTestNotFound() {

        Mockito.when(testsService.getById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsController.getGrades(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getGradesFailModuleGradeNotFound() {

        Mockito.when(testsService.getById(BAD_TEST_ID)).thenThrow(ModuleGradeNotFoundException.class);

        Assertions.assertThrows(ModuleGradeNotFoundException.class, () -> testsController.getGrades(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getUsersTestsSuccess() {
        List<Test> tests = new ArrayList<>();

        Mockito.when(testsService
                .getByUserId(GOOD_USER_ID, pageable)).thenReturn(tests);

        Assertions.assertEquals(Lists.emptyList(),
                testsController.getUsersTests(GOOD_USER_ID, null, 1, 10));
    }

    @org.junit.jupiter.api.Test
    void getUsersTestsFailUserNotFound() {
        Mockito.when(testsService.getByUserId(BAD_USER_ID, pageable)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> testsController.getUsersTests(BAD_USER_ID, null, 1, 10));
    }

    @org.junit.jupiter.api.Test
    void getUsersTestsWithLevelSuccess() {
        List<Test> tests = new ArrayList<>();

        Mockito.when(testsService.getTestsByUserIdAndLevel(GOOD_USER_ID, Levels.A1, pageable)).thenReturn(tests);

        Assertions.assertEquals(Lists.emptyList(),
                testsController.getUsersTests(GOOD_USER_ID, Levels.A1, 1, 10));
    }


    @org.junit.jupiter.api.Test
    void getUsersTestsWithLevelFailUserNotFound() {
        Mockito.when(testsService.getTestsByUserIdAndLevel(BAD_USER_ID, Levels.A1, pageable))
                .thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> testsController.getUsersTests(BAD_USER_ID, Levels.A1, 1, 10));
    }

    @org.junit.jupiter.api.Test
    void getTestForVerificationNotFound() {
        Mockito.when(testsService.startTestVerification(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsController.getTestForVerification(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getTestForVerificationSuccess() {
        Mockito.when(testsService.startTestVerification(GOOD_TEST_ID)).thenReturn(test);
        Mockito.when(verificationConverter.convertToVerificationDTO(test)).thenReturn(testVerificationDTO);

        Assertions.assertEquals(testVerificationDTO, testsController.getTestForVerification(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getUnverifiedTestsForCurrentCoachSuccess() {
        TestInfo testInfo = new TestInfo();

        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(customUserDetails);
            Mockito.when(customUserDetails.getId()).thenReturn(GOOD_USER_ID);
            Mockito.when(testsService.getAllUnverifiedTestsByCoach(GOOD_USER_ID, pageable))
                    .thenReturn(Lists.list(test));
            Mockito.when(testConverter.convertToInfo(test)).thenReturn(testInfo);

            Assertions.assertEquals(Lists.list(testInfo), testsController.getUnverifiedTestsForCurrentCoach(1, 10));
        }
    }

    @org.junit.jupiter.api.Test
    void assignSuccess() {
        AssignTestRequest request = new AssignTestRequest(Levels.A1, Instant.now(), Priority.MEDIUM);

        Mockito.when(testsService.assignForUser(GOOD_USER_ID, Levels.A1, request.getDeadline(), Priority.MEDIUM))
                .thenReturn(1L);

        Assertions.assertEquals(1L, testsController.assign(GOOD_USER_ID, request));
    }

    @org.junit.jupiter.api.Test
    void assignFail() {
        AssignTestRequest request = new AssignTestRequest(Levels.A1, Instant.now(), Priority.LOW);

        Mockito.when(testsService.assignForUser(BAD_USER_ID, Levels.A1, request.getDeadline(), Priority.LOW))
                .thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> testsController.assign(BAD_USER_ID, request));
    }

    @org.junit.jupiter.api.Test
    void deassignSuccess() {
        testsController.deassign(GOOD_TEST_ID);

        verify(testsService).deassign(GOOD_TEST_ID);
    }

    @org.junit.jupiter.api.Test
    void deassignFail() {
        doThrow(TestNotFoundException.class).when(testsService).deassign(BAD_TEST_ID);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsController.deassign(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void startNotAssignedSuccess() {
        Test test = EntityCreatorUtil.createTest(new User(), new Level());
        TestDTO testDTO = new TestDTO(test);

        try (MockedStatic<JwtTokenUtil> builderMockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
            builderMockedStatic.when(JwtTokenUtil::extractUserDetails).thenReturn(customUserDetails);
            Mockito.when(customUserDetails.getId()).thenReturn(1L);
            Mockito.when(testsService.startForUser(1L, Levels.A1)).thenReturn(1L);
            Mockito.when(testsService.start(1L)).thenReturn(test);
            Mockito.when(testConverter.convertToDTO(test)).thenReturn(testDTO);

            Assertions.assertEquals(testDTO, testsController.startNotAssigned(Levels.A1));
        }
    }

    @org.junit.jupiter.api.Test
    void startNotAssignedFail() {
        try (MockedStatic<JwtTokenUtil> builderMockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
            builderMockedStatic.when(JwtTokenUtil::extractUserDetails).thenReturn(customUserDetails);
            Mockito.when(customUserDetails.getId()).thenReturn(1L);
            Mockito.when(testsService.startForUser(1L, Levels.A1)).thenThrow(TestsLimitExceededException.class);

            Assertions.assertThrows(TestsLimitExceededException.class,
                    () -> testsController.startNotAssigned(Levels.A1));
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
        testsController.finish(GOOD_TEST_ID);

        verify(testsService).finish(anyLong(), any(Instant.class));
    }

    @org.junit.jupiter.api.Test
    void finishFail() {
        doThrow(TestNotFoundException.class).when(testsService).finish(anyLong(), any(Instant.class));

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

        Assertions.assertThrows(TestNotFoundException.class, () -> testsController.deassignCoach(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getUnverifiedTests() {
        Mockito.when(testsService.getAllUnverifiedTests(pageable)).thenReturn(List.of());
        Assertions.assertEquals(List.of(), testsController.getUnverifiedTests(1, 10));
    }
}
