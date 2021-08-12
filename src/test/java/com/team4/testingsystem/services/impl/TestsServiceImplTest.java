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
import com.team4.testingsystem.services.LevelService;
import com.team4.testingsystem.services.TestEvaluationService;
import com.team4.testingsystem.services.UsersService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    List<Test> tests;


    @InjectMocks
    TestsServiceImpl testsService;

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

        Mockito.when(testsRepository.getAllByUser(user)).thenReturn(tests);

        Assertions.assertEquals(tests, testsService.getByUserId(GOOD_USER_ID));
    }

    @org.junit.jupiter.api.Test
    void getByUserIdFailUserNotFound() {
        Mockito.when(usersService.getUserById(BAD_USER_ID)).thenThrow(UserNotFoundException.class);
        Assertions.assertThrows(UserNotFoundException.class, () -> testsService.getByUserId(BAD_USER_ID));
    }

    @org.junit.jupiter.api.Test
    void getAllUsersAndAssignedTestsNoTests() {
        User user = EntityCreatorUtil.createUser();

        Mockito.when(usersService.getAll()).thenReturn(Lists.list(user));
        Mockito.when(testsRepository.getByStatuses(new Status[] {Status.ASSIGNED}))
                .thenReturn(Lists.emptyList());

        Assertions.assertEquals(Lists.list(new UserTest(user, null)), testsService.getAllUsersAndAssignedTests());
    }

    @org.junit.jupiter.api.Test
    void getAllUsersAndAssignedTestsSuccess() {
        User user = EntityCreatorUtil.createUser();
        Test test = EntityCreatorUtil.createTest(user, EntityCreatorUtil.createLevel());

        Mockito.when(usersService.getAll()).thenReturn(Lists.list(user));
        Mockito.when(testsRepository.getByStatuses(new Status[] {Status.ASSIGNED}))
                .thenReturn(Lists.list(test));

        Assertions.assertEquals(Lists.list(new UserTest(user, test)), testsService.getAllUsersAndAssignedTests());
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

        Mockito.when(test.getStartedAt()).thenReturn(LocalDateTime.now());

        Assertions.assertThrows(
                TestsLimitExceededException.class, () -> testsService.startForUser(GOOD_USER_ID, Levels.A1));
    }

    @org.junit.jupiter.api.Test
    void assignFail() {
        Level level = EntityCreatorUtil.createLevel();
        LocalDateTime deadline = LocalDateTime.now();

        Mockito.when(levelService.getLevelByName(level.getName())).thenReturn(level);
        Mockito.when(usersService.getUserById(BAD_USER_ID)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> testsService.assignForUser(BAD_USER_ID, Levels.A1, deadline, Priority.LOW));
    }

    @org.junit.jupiter.api.Test
    void assignForUserSuccess() {
        Level level = EntityCreatorUtil.createLevel();
        LocalDateTime deadline = LocalDateTime.now();

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
    void deassignSuccessTestWasStarted(){
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        Mockito.when(test.getStartedAt()).thenReturn(LocalDateTime.now());

        testsService.deassign(GOOD_TEST_ID);

        verify(testsRepository).deassign(GOOD_TEST_ID);
    }


    @org.junit.jupiter.api.Test
    void deassignSuccessTestWasNotStarted(){
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        Mockito.when(test.getStartedAt()).thenReturn(null);

        testsService.deassign(GOOD_TEST_ID);

        verify(testsRepository).removeById(GOOD_TEST_ID);
    }


    @org.junit.jupiter.api.Test
    void deassignFail(){
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, ()->testsService.deassign(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void startSuccess() {
        User user = EntityCreatorUtil.createUser();
        Level level = EntityCreatorUtil.createLevel();

        Mockito.when(testsRepository.start(any(), anyLong())).thenReturn(1);
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
        Mockito.when(testGeneratingService.formTest(any())).thenReturn(test);


        try (MockedConstruction<Timer> mocked = Mockito.mockConstruction(Timer.class,
                (mock, context) -> {
                    Mockito.when(mock.getTest()).thenReturn(test);
                })) {
            Mockito.when(test.getId()).thenReturn(GOOD_TEST_ID);

            Mockito.when(test.getFinishTime()).thenReturn(LocalDateTime.now());

            Test result = testsService.start(GOOD_TEST_ID);
            verify(testsRepository).start(any(LocalDateTime.class), anyLong());
            verify(timerRepository).save(any(Timer.class));
            Assertions.assertDoesNotThrow(() -> testsService.start(GOOD_TEST_ID));
            Assertions.assertEquals(test, result);
        }
    }

    @org.junit.jupiter.api.Test
    void startAllTimers(){
        testsService.startAllTimers();

        verify(timerRepository).findAll();
    }

    @org.junit.jupiter.api.Test
    void startFail() {
        Mockito.when(testsRepository.start(any(), anyLong())).thenReturn(0);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.start(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void finishSuccess() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        testsService.finish(GOOD_TEST_ID);

        verify(testEvaluationService).countScoreBeforeCoachCheck(test);

        verify(testsRepository).finish(any(LocalDateTime.class), anyLong());

        Assertions.assertDoesNotThrow(() -> testsService.finish(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void finishFail() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.finish(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void updateEvaluationSuccess() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        testsService.update(GOOD_TEST_ID);

        verify(testsRepository).updateEvaluation(any(LocalDateTime.class), anyLong());

        verify(testEvaluationService).updateScoreAfterCoachCheck(test);

        Assertions.assertDoesNotThrow(() -> testsService.update(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void updateEvaluationFail() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsService.update(BAD_TEST_ID));
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
        Mockito.when(testsRepository.getByStatuses(any())).thenReturn(tests);
        Assertions.assertEquals(tests, testsService.getByStatuses(statuses));
    }
}
