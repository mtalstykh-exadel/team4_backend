package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.Timer;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserTest;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Priority;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.CoachAssignmentFailException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.TestsLimitExceededException;
import com.team4.testingsystem.exceptions.UserNotFoundException;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.TimerRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.LevelService;
import com.team4.testingsystem.services.TestEvaluationService;
import com.team4.testingsystem.services.UsersService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestsServiceImplTest {

    final long GOOD_TEST_ID = 1L;
    final long GOOD_USER_ID = 111L;
    final long BAD_TEST_ID = 42L;
    final long BAD_USER_ID = 424242L;

    @Mock
    Test test;

    @Mock
    Test.Builder builder;

    @Mock
    User user;

    @Mock
    UsersService usersService;

    @Mock
    LevelService levelService;

    @Mock
    TestsRepository testsRepository;

    @Mock
    TimerRepository timerRepository;

    @Mock
    TestGeneratingServiceImpl testGeneratingService;

    @Mock
    TestEvaluationService testEvaluationService;

    @Mock
    Stream<Test> stream;

    @Mock
    CustomUserDetails userDetails;

    @Mock
    List<Test> tests;

    @InjectMocks
    TestsServiceImpl testsService;

    private final Pageable pageable = PageRequest.of(1, 10);

    @org.junit.jupiter.api.Test
    void getByIdSuccess() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
        Assertions.assertEquals(test, testsService.getById(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getByIdFail() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);
        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.getById(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getByUserIdSuccess() {
        Mockito.when(usersService.getUserById(GOOD_USER_ID)).thenReturn(user);

        Mockito.when(testsRepository.getAllByUser(user, pageable)).thenReturn(tests);

        Assertions.assertEquals(tests, testsService.getByUserId(GOOD_USER_ID, pageable));
    }

    @org.junit.jupiter.api.Test
    void getByUserIdFailUserNotFound() {
        Mockito.when(usersService.getUserById(BAD_USER_ID)).thenThrow(UserNotFoundException.class);
        Assertions.assertThrows(UserNotFoundException.class, () -> testsService.getByUserId(BAD_USER_ID, pageable));
    }

    @org.junit.jupiter.api.Test
    void getAllUsersAndAssignedTestsNoTests() {
        User user = EntityCreatorUtil.createUser();

        Mockito.when(usersService.getAll()).thenReturn(Lists.list(user));
        Mockito.when(testsRepository.getByStatuses(new Status[]{Status.ASSIGNED}, pageable))
            .thenReturn(Lists.emptyList());

        Assertions.assertEquals(Lists.list(new UserTest(user, null)), testsService.getAllUsersAndAssignedTests(pageable));
    }

    @org.junit.jupiter.api.Test
    void getAllUsersAndAssignedTestsSuccess() {
        User user = EntityCreatorUtil.createUser();
        Test test = EntityCreatorUtil.createTest(user, EntityCreatorUtil.createLevel());

        Mockito.when(usersService.getAll()).thenReturn(Lists.list(user));
        Mockito.when(testsRepository.getByStatuses(new Status[]{Status.ASSIGNED}, pageable))
            .thenReturn(Lists.list(test));

        Assertions.assertEquals(Lists.list(new UserTest(user, test)), testsService.getAllUsersAndAssignedTests(pageable));
    }

    @org.junit.jupiter.api.Test
    void getAllUnverifiedTestsSuccess() {
        Status[] statuses = {Status.COMPLETED, Status.IN_VERIFICATION};

        Mockito.when(userDetails.getId()).thenReturn(1L);

        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);

            Mockito.when(testsRepository.getByStatuses(statuses, pageable))
                .thenReturn(tests);
            Mockito.when(tests.stream()).thenReturn(stream);

            Mockito.when(stream.filter(any())).thenReturn(stream);

            Mockito.when(stream.collect(any())).thenReturn(tests);

            Assertions.assertEquals(tests, testsService.getAllUnverifiedTests(pageable));
        }
    }

    @org.junit.jupiter.api.Test
    void getAllUnverifiedTestsByCoachSuccess() {
        Status[] statuses = {Status.COMPLETED, Status.IN_VERIFICATION};
        Mockito.when(testsRepository.getAllByAssignedCoachAndStatuses(GOOD_USER_ID, statuses, pageable))
                .thenReturn(Lists.list(test));

        Assertions.assertEquals(Lists.list(test), testsService.getAllUnverifiedTestsByCoach(GOOD_USER_ID, pageable));
    }

    @org.junit.jupiter.api.Test
    void startForUserSuccess() {
        Mockito.when(usersService.getUserById(GOOD_USER_ID)).thenReturn(user);

        Mockito.when(testsRepository.getSelfStartedByUserAfter(any(), any())).thenReturn(tests);

        Mockito.when(tests.size()).thenReturn(-1);

        Level level = EntityCreatorUtil.createLevel();
        Mockito.when(levelService.getLevelByName(level.getName())).thenReturn(level);

        try (MockedStatic<Test> builderMockedStatic = Mockito.mockStatic(Test.class)) {

            builderMockedStatic.when(Test::builder).thenReturn(builder);

            Mockito.when(builder.user(any())).thenReturn(builder);
            Mockito.when(builder.startedAt(any())).thenReturn(builder);
            Mockito.when(builder.status(any())).thenReturn(builder);
            Mockito.when(builder.priority(Priority.LOW)).thenReturn(builder);
            Mockito.when(builder.level(any())).thenReturn(builder);
            Mockito.when(builder.build()).thenReturn(test);

            Mockito.when(test.getId()).thenReturn(1L);

            Assertions.assertEquals(1L, testsService.startForUser(GOOD_USER_ID, Levels.A1));
        }
    }

    @org.junit.jupiter.api.Test
    void startForUserFailUserNotFound() {
        Mockito.when(usersService.getUserById(BAD_USER_ID)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(
                UserNotFoundException.class, () -> testsService.startForUser(BAD_USER_ID, Levels.A1));
    }

    @org.junit.jupiter.api.Test
    void startForUserFailTestsLimit() {
        Mockito.when(usersService.getUserById(GOOD_USER_ID)).thenReturn(user);

        Mockito.when(testsRepository.getSelfStartedByUserAfter(any(), any())).thenReturn(tests);

        Mockito.when(tests.size()).thenReturn(1000000007);

        Mockito.when(tests.get(0)).thenReturn(test);

        Mockito.when(test.getStartedAt()).thenReturn(Instant.now());

        Assertions.assertThrows(
                TestsLimitExceededException.class, () -> testsService.startForUser(GOOD_USER_ID, Levels.A1));
    }

    @org.junit.jupiter.api.Test
    void assignFail() {
        Level level = EntityCreatorUtil.createLevel();
        Instant deadline = Instant.now();

        Mockito.when(levelService.getLevelByName(level.getName())).thenReturn(level);
        Mockito.when(usersService.getUserById(BAD_USER_ID)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> testsService.assignForUser(BAD_USER_ID, Levels.A1, deadline, Priority.LOW));
    }

    @org.junit.jupiter.api.Test
    void assignForUserSuccess() {
        Level level = EntityCreatorUtil.createLevel();
        Instant deadline = Instant.now();

        Mockito.when(levelService.getLevelByName(level.getName())).thenReturn(level);
        Mockito.when(usersService.getUserById(GOOD_USER_ID)).thenReturn(user);

        try (MockedStatic<Test> builderMockedStatic = Mockito.mockStatic(Test.class)) {

            builderMockedStatic.when(Test::builder).thenReturn(builder);

            Mockito.when(builder.user(any())).thenReturn(builder);
            Mockito.when(builder.assignedAt(any())).thenReturn(builder);
            Mockito.when(builder.deadline(deadline)).thenReturn(builder);
            Mockito.when(builder.status(any())).thenReturn(builder);
            Mockito.when(builder.priority(any())).thenReturn(builder);
            Mockito.when(builder.level(any())).thenReturn(builder);
            Mockito.when(builder.build()).thenReturn(test);

            Mockito.when(test.getId()).thenReturn(1L);

            Assertions.assertEquals(1L,
                    testsService.assignForUser(GOOD_USER_ID, Levels.A1, deadline, Priority.HIGH));
        }
    }

    @org.junit.jupiter.api.Test
    void deassignSuccessTestWasStarted() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        Mockito.when(test.getStartedAt()).thenReturn(Instant.now());

        testsService.deassign(GOOD_TEST_ID);

        verify(testsRepository).deassign(GOOD_TEST_ID);
    }

    @org.junit.jupiter.api.Test
    void deassignSuccessTestWasNotStarted() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        Mockito.when(test.getStartedAt()).thenReturn(null);

        testsService.deassign(GOOD_TEST_ID);

        verify(testsRepository).removeById(GOOD_TEST_ID);
    }

    @org.junit.jupiter.api.Test
    void deassignFail() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.deassign(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void startSuccess() {
        Mockito.when(testsRepository.start(any(), anyLong())).thenReturn(1);
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
        Mockito.when(testGeneratingService.formTest(any())).thenReturn(test);

        try (MockedConstruction<Timer> mocked = Mockito.mockConstruction(Timer.class,
                (mock, context) -> {
                    Mockito.when(mock.getTest()).thenReturn(test);
                })) {
            Mockito.when(test.getId()).thenReturn(GOOD_TEST_ID);

            Mockito.when(test.getFinishTime()).thenReturn(Instant.now());

            Test result = testsService.start(GOOD_TEST_ID);
            verify(testsRepository).start(any(Instant.class), anyLong());
            verify(timerRepository).save(any(Timer.class));
            Assertions.assertDoesNotThrow(() -> testsService.start(GOOD_TEST_ID));
            Assertions.assertEquals(test, result);
        }
    }

    @org.junit.jupiter.api.Test
    void startFail() {
        Mockito.when(testsRepository.start(any(), anyLong())).thenReturn(0);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.start(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void startAllTimersSuccess() {
        testsService.startAllTimers();

        verify(timerRepository).findAll();
    }

    @org.junit.jupiter.api.Test
    void finishSuccess() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        Mockito.when(test.getStatus()).thenReturn(Status.STARTED);

        testsService.finish(GOOD_TEST_ID, Instant.now());

        verify(testEvaluationService).countScoreBeforeCoachCheck(test);

        verify(testsRepository).finish(any(Instant.class), anyLong());

        Assertions.assertDoesNotThrow(() -> testsService.finish(GOOD_TEST_ID, Instant.now()));
    }

    @org.junit.jupiter.api.Test
    void finishFail() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.finish(BAD_TEST_ID, Instant.now()));
    }

    @org.junit.jupiter.api.Test
    void coachSubmitSuccess() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        testsService.coachSubmit(GOOD_TEST_ID);

        verify(testsRepository).updateEvaluation(any(Instant.class), anyLong());

        verify(testEvaluationService).updateScoreAfterCoachCheck(test);

        Assertions.assertDoesNotThrow(() -> testsService.coachSubmit(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void coachSubmitFail() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsService.coachSubmit(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void startTestVerificationNotFound() {
        Mockito.when(testsRepository.updateStatusByTestId(BAD_TEST_ID, Status.IN_VERIFICATION)).thenReturn(0);
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.startTestVerification(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void startTestVerificationSuccess() {
        Mockito.when(testsRepository.updateStatusByTestId(GOOD_TEST_ID, Status.IN_VERIFICATION)).thenReturn(1);
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        Assertions.assertEquals(test, testsService.startTestVerification(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void saveSuccess() {
        Mockito.when(testsRepository.save(test)).thenReturn(test);
        Assertions.assertEquals(test, testsService.save(test));
    }

    @org.junit.jupiter.api.Test
    void saveFail() {
        Mockito.when(testsRepository.save(test)).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> testsService.save(test));
    }

    @org.junit.jupiter.api.Test
    void assignCoachSuccess() {
        Mockito.when(usersService.getUserById(GOOD_USER_ID)).thenReturn(user);

        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        Mockito.when(test.getUser()).thenReturn(user);

        Mockito.when(user.getId()).thenReturn(GOOD_USER_ID + 1);

        testsService.assignCoach(GOOD_TEST_ID, GOOD_USER_ID);

        verify(testsRepository).assignCoach(user, GOOD_TEST_ID);

        Assertions.assertDoesNotThrow(() -> testsService.assignCoach(GOOD_TEST_ID, GOOD_USER_ID));
    }

    @org.junit.jupiter.api.Test
    void assignCoachFailUserNotFound() {
        Mockito.when(usersService.getUserById(BAD_USER_ID)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> testsService.assignCoach(BAD_TEST_ID, BAD_USER_ID));
    }

    @org.junit.jupiter.api.Test
    void assignCoachFailTestNotFound() {
        Mockito.when(usersService.getUserById(GOOD_USER_ID)).thenReturn(user);

        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        Mockito.when(test.getUser()).thenReturn(user);

        Mockito.when(user.getId()).thenReturn(GOOD_USER_ID);

        Assertions.assertThrows(CoachAssignmentFailException.class,
                () -> testsService.assignCoach(GOOD_TEST_ID, GOOD_USER_ID));
    }

    @org.junit.jupiter.api.Test
    void assignCoachFailSelfAssignment() {
        Mockito.when(usersService.getUserById(GOOD_USER_ID)).thenReturn(user);

        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.assignCoach(BAD_TEST_ID, GOOD_USER_ID));
    }

    @org.junit.jupiter.api.Test
    void deassignCoachSuccess() {
        Mockito.when(testsRepository.deassignCoach(GOOD_TEST_ID)).thenReturn(1);

        testsService.deassignCoach(GOOD_TEST_ID);

        verify(testsRepository).deassignCoach(GOOD_TEST_ID);

        Assertions.assertDoesNotThrow(() -> testsService.deassignCoach(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void deassignCoachFail() {
        Mockito.when(testsRepository.deassignCoach(BAD_TEST_ID)).thenReturn(0);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.deassignCoach(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getByStatus() {
        Status[] statuses = {Status.COMPLETED, Status.IN_VERIFICATION};
        Mockito.when(testsRepository.getByStatuses(statuses, pageable)).thenReturn(tests);
        Assertions.assertEquals(tests, testsService.getByStatuses(statuses, pageable));
    }
}
