package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.Timer;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserTest;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.enums.Priority;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.NotEnoughQuestionsException;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.TestsLimitExceededException;
import com.team4.testingsystem.exceptions.UserNotFoundException;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.TimerRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.LevelService;
import com.team4.testingsystem.services.NotificationService;
import com.team4.testingsystem.services.RestrictionsService;
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
    private Test test;

    @Mock
    private Test.Builder builder;

    @Mock
    private User user;

    @Mock
    private UsersService usersService;

    @Mock
    private LevelService levelService;

    @Mock
    private TestsRepository testsRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private TimerRepository timerRepository;

    @Mock
    private TestGeneratingServiceImpl testGeneratingService;

    @Mock
    private TestEvaluationService testEvaluationService;

    @Mock
    private Stream<Test> stream;

    @Mock
    private CustomUserDetails userDetails;

    @Mock
    private List<Test> tests;

    @Mock
    private RestrictionsService restrictionsService;

    @InjectMocks
    private TestsServiceImpl testsService;

    private final Pageable pageable = PageRequest.of(1, 10);

    @org.junit.jupiter.api.Test
    void getByIdSuccess() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        Assertions.assertEquals(test, testsService.getById(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getByIdWithRestrictions() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(GOOD_USER_ID);
            Test result = testsService.getByIdWithRestrictions(GOOD_TEST_ID);

            verify(restrictionsService).checkOwnerIsCurrentUser(test, GOOD_USER_ID);
            verify(restrictionsService).checkStatus(test, Status.STARTED);
            Assertions.assertEquals(test, result);
        }

    }

    @org.junit.jupiter.api.Test
    void getByIdFail() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.getById(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void getByUserIdSuccess() {
        Mockito.when(testsRepository.getAllByUserId(GOOD_USER_ID, pageable)).thenReturn(tests);

        Assertions.assertEquals(tests, testsService.getByUserId(GOOD_USER_ID, null, pageable));
    }

    @org.junit.jupiter.api.Test
    void getAllUsersAndAssignedTestsNoTests() {
        User user = EntityCreatorUtil.createUser();
        Mockito.when(usersService.getAll(pageable)).thenReturn(Lists.list(user));

        Assertions.assertEquals(Lists.list(new UserTest(user, null)),
                testsService.getAllUsersAndAssignedTests(null, pageable));
    }

    @org.junit.jupiter.api.Test
    void getAllUsersAndAssignedTestsSuccess() {
        User user = EntityCreatorUtil.createUser();
        Test test = EntityCreatorUtil.createTest(user, EntityCreatorUtil.createLevel());
        Mockito.when(testsRepository.getAssignedTestByUserId(user.getId()))
                .thenReturn(Optional.ofNullable(test));
        Mockito.when(usersService.getAll(pageable)).thenReturn(Lists.list(user));

        Assertions.assertEquals(Lists.list(new UserTest(user, test)),
                testsService.getAllUsersAndAssignedTests(null, pageable));
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
    void createNotAssignedSuccess() {
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
            Mockito.when(builder.listeningAttempts(3)).thenReturn(builder);
            Mockito.when(builder.build()).thenReturn(test);
            Mockito.when(test.getId()).thenReturn(1L);
            testsService.createNotAssigned(GOOD_USER_ID, Levels.A1);

            verify(restrictionsService).checkHasNoStartedTests(GOOD_USER_ID);
            Assertions.assertEquals(1L, testsService.createNotAssigned(GOOD_USER_ID, Levels.A1));
        }
    }

    @org.junit.jupiter.api.Test
    void createNotAssignedFailUserNotFound() {
        Mockito.when(usersService.getUserById(BAD_USER_ID)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> testsService.createNotAssigned(BAD_USER_ID, Levels.A1));
    }

    @org.junit.jupiter.api.Test
    void createNotAssignedFailTestsLimit() {
        Mockito.when(usersService.getUserById(GOOD_USER_ID)).thenReturn(user);
        Mockito.when(testsRepository.getSelfStartedByUserAfter(any(), any())).thenReturn(tests);
        Mockito.when(tests.size()).thenReturn(1000000007);
        Mockito.when(tests.get(0)).thenReturn(test);
        Mockito.when(test.getStartedAt()).thenReturn(Instant.now());

        Assertions.assertThrows(TestsLimitExceededException.class,
                () -> testsService.createNotAssigned(GOOD_USER_ID, Levels.A1));
    }

    @org.junit.jupiter.api.Test
    void createAssignedFail() {
        Instant deadline = Instant.now();
        Mockito.when(usersService.getUserById(BAD_USER_ID)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> testsService.createAssigned(BAD_USER_ID, Levels.A1, deadline, Priority.LOW));
        verify(notificationService, Mockito.never()).create(any(), any(), any());
    }

    @org.junit.jupiter.api.Test
    void createAssignedSuccess() {
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
            Mockito.when(builder.listeningAttempts(3)).thenReturn(builder);
            Mockito.when(builder.build()).thenReturn(test);
            Mockito.when(test.getId()).thenReturn(1L);
            Mockito.when(test.getUser()).thenReturn(user);

            Assertions.assertEquals(1L,
                    testsService.createAssigned(GOOD_USER_ID, Levels.A1, deadline, Priority.HIGH));
            verify(restrictionsService).checkNotSelfAssign(user);
            verify(restrictionsService).checkHasNoAssignedTests(user);
            verify(notificationService).create(NotificationType.TEST_ASSIGNED, user, test);
            verify(restrictionsService).checkNotSelfAssign(user);
            verify(restrictionsService).checkHasNoAssignedTests(user);
        }
    }

    @org.junit.jupiter.api.Test
    void deassignSuccessTestWasStarted() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
        Mockito.when(test.getUser()).thenReturn(user);
        Mockito.when(test.getStartedAt()).thenReturn(Instant.now());
        testsService.deassign(GOOD_TEST_ID);

        verify(testsRepository).deassign(GOOD_TEST_ID);
        verify(notificationService).create(NotificationType.TEST_DEASSIGNED, user, test);
    }

    @org.junit.jupiter.api.Test
    void deassignSuccessTestWasNotStarted() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
        Mockito.when(test.getUser()).thenReturn(user);
        Mockito.when(test.getStartedAt()).thenReturn(null);
        testsService.deassign(GOOD_TEST_ID);

        verify(restrictionsService).checkNotSelfDeassign(test.getUser());
        verify(restrictionsService).checkIsAssigned(test);
        verify(testsRepository).archiveById(GOOD_TEST_ID);
        verify(notificationService).create(NotificationType.TEST_DEASSIGNED, user, test);
    }

    @org.junit.jupiter.api.Test
    void deassignFail() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.deassign(BAD_TEST_ID));
        verify(notificationService, Mockito.never()).create(any(), any(), any());
    }

    @org.junit.jupiter.api.Test
    void startNotAssignedSuccess() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
        Mockito.when(testGeneratingService.formTest(any())).thenReturn(test);
        Mockito.when(test.getUser()).thenReturn(user);
        try (MockedConstruction<Timer> mocked = Mockito.mockConstruction(Timer.class,
                (mock, context) -> {
                    Mockito.when(mock.getTest()).thenReturn(test);
                })) {
            Mockito.when(test.getId()).thenReturn(GOOD_TEST_ID);
            Mockito.when(test.getFinishTime()).thenReturn(Instant.now());
            Test result = testsService.startNotAssigned(GOOD_TEST_ID);

            Assertions.assertEquals(test, result);
            verify(timerRepository).save(any(Timer.class));
            verify(notificationService).create(NotificationType.TEST_STARTED, user, test);
        }
    }

    @org.junit.jupiter.api.Test
    void startNotAssignedFailNoQuestions() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
        Mockito.when(testGeneratingService.formTest(any(Test.class))).thenThrow(NotEnoughQuestionsException.class);

        Mockito.when(test.getStatus()).thenReturn(Status.STARTED);

        Mockito.when(test.getId()).thenReturn(GOOD_TEST_ID);



        Assertions.assertThrows(NotEnoughQuestionsException.class,
                () ->  testsService.startNotAssigned(GOOD_TEST_ID));

        verify(testsRepository).archiveById(GOOD_TEST_ID);
    }

    @org.junit.jupiter.api.Test
    void startNotAssignedFailTestNotFound() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsService.startNotAssigned(BAD_TEST_ID));
        verify(notificationService, Mockito.never()).create(any(), any(), any());
    }

    @org.junit.jupiter.api.Test
    void startAssignedSuccess() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(GOOD_USER_ID);
            Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
            Mockito.when(testGeneratingService.formTest(any())).thenReturn(test);
            Mockito.when(test.getUser()).thenReturn(user);
            try (MockedConstruction<Timer> mocked = Mockito.mockConstruction(Timer.class,
                    (mock, context) -> {
                        Mockito.when(mock.getTest()).thenReturn(test);
                    })) {
                Mockito.when(test.getId()).thenReturn(GOOD_TEST_ID);
                Mockito.when(test.getFinishTime()).thenReturn(Instant.now());
                Test result = testsService.startAssigned(GOOD_TEST_ID);

                Assertions.assertEquals(test, result);
                verify(restrictionsService).checkOwnerIsCurrentUser(test, GOOD_USER_ID);
                verify(restrictionsService).checkStatus(test, Status.ASSIGNED);
                verify(restrictionsService).checkHasNoStartedTests(GOOD_USER_ID);
                verify(timerRepository).save(any(Timer.class));
                verify(notificationService).create(NotificationType.TEST_STARTED, user, test);
            }
        }
    }

    @org.junit.jupiter.api.Test
    void startAssignedFail() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.startAssigned(BAD_TEST_ID));
        verify(notificationService, Mockito.never()).create(any(), any(), any());
    }


    @org.junit.jupiter.api.Test
    void startAllTimersSuccess() {
        testsService.startAllTimers();

        verify(timerRepository).findAll();
    }

    @org.junit.jupiter.api.Test
    void selfFinishSuccess() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(GOOD_USER_ID);
            Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
            Mockito.when(test.getStatus()).thenReturn(Status.STARTED);
            testsService.selfFinish(GOOD_TEST_ID);
            restrictionsService.checkOwnerIsCurrentUser(test, GOOD_USER_ID);
            restrictionsService.checkStatus(test, Status.STARTED);

            verify(testEvaluationService).countScoreBeforeCoachCheck(test);
            verify(testsRepository).finish(any(Instant.class), anyLong());
            verify(timerRepository).deleteByTestId(GOOD_TEST_ID);
            Assertions.assertDoesNotThrow(() -> testsService.selfFinish(GOOD_TEST_ID));
        }
    }

    @org.junit.jupiter.api.Test
    void selfFinishFail() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsService.selfFinish(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void coachSubmitSuccess() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
        Mockito.when(test.getUser()).thenReturn(user);

        Assertions.assertDoesNotThrow(() -> testsService.coachSubmit(GOOD_TEST_ID));
        verify(testsRepository).coachSubmit(any(Instant.class), anyLong());
        verify(testEvaluationService).updateScoreAfterCoachCheck(test);
        verify(restrictionsService).checkCoachIsCurrentUser(test);
        verify(restrictionsService).checkStatus(test, Status.IN_VERIFICATION);
        verify(notificationService).create(NotificationType.TEST_VERIFIED, user, test);
    }

    @org.junit.jupiter.api.Test
    void coachSubmitFail() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsService.coachSubmit(BAD_TEST_ID));
        verify(notificationService, Mockito.never()).create(any(), any(), any());
    }

    @org.junit.jupiter.api.Test
    void startTestVerificationNotFound() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsService.startTestVerification(BAD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void startTestVerificationSuccess() {
        Mockito.when(testsRepository.updateStatusByTestId(GOOD_TEST_ID, Status.IN_VERIFICATION)).thenReturn(1);
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
        Mockito.when(test.getStatus()).thenReturn(Status.COMPLETED);

        Assertions.assertEquals(test, testsService.startTestVerification(GOOD_TEST_ID));
        verify(restrictionsService).checkCoachIsCurrentUser(test);
        verify(restrictionsService).checkNotVerified(test);
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
        testsService.assignCoach(GOOD_TEST_ID, GOOD_USER_ID);

        verify(restrictionsService).checkHasNoAssignedCoaches(test);
        verify(restrictionsService).checkNotSelfAssignmentCoach(test, GOOD_USER_ID);
        verify(restrictionsService).checkStatus(test, Status.COMPLETED);
        verify(restrictionsService).checkNotSelfAssignAdmin(test);
        verify(testsRepository).assignCoach(user, GOOD_TEST_ID);
        verify(notificationService).create(NotificationType.COACH_ASSIGNED, user, test);
    }

    @org.junit.jupiter.api.Test
    void assignCoachFailUserNotFound() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
        Mockito.when(usersService.getUserById(BAD_USER_ID)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> testsService.assignCoach(GOOD_TEST_ID, BAD_USER_ID));
        verify(notificationService, Mockito.never()).create(any(), any(), any());
    }

    @org.junit.jupiter.api.Test
    void assignCoachFailTestNotFound() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsService.assignCoach(GOOD_TEST_ID, GOOD_USER_ID));
        verify(notificationService, Mockito.never()).create(any(), any(), any());
    }

    @org.junit.jupiter.api.Test
    void deassignCoachSuccess() {
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));
        Mockito.when(test.getCoach()).thenReturn(user);
        testsService.deassignCoach(GOOD_TEST_ID);

        verify(restrictionsService).checkHasAssignedCoach(test);
        verify(restrictionsService).checkNotVerifiedForCoachDeassign(test);
        verify(restrictionsService).checkNotSelfDeassignAdmin(test);
        verify(testsRepository).deassignCoach(GOOD_TEST_ID);
        verify(notificationService).create(NotificationType.COACH_DEASSIGNED, user, test);
        Assertions.assertDoesNotThrow(() -> testsService.deassignCoach(GOOD_TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void deassignCoachFail() {
        Mockito.when(testsRepository.findById(BAD_TEST_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsService.deassignCoach(BAD_TEST_ID));
        verify(notificationService, Mockito.never()).create(any(), any(), any());
    }

    @org.junit.jupiter.api.Test
    void getByStatus() {
        Status[] statuses = {Status.COMPLETED, Status.IN_VERIFICATION};
        Mockito.when(testsRepository.getByStatuses(statuses, pageable)).thenReturn(tests);

        Assertions.assertEquals(tests, testsService.getByStatuses(statuses, pageable));
    }

    @org.junit.jupiter.api.Test
    void getTestsByUserIdAndLevel() {
        List<Test> tests = List.of(new Test());
        Mockito.when(testsRepository.getAllByUserAndLevel(GOOD_USER_ID, Levels.A1.name(), pageable))
                .thenReturn(tests);

        Assertions.assertEquals(tests, testsService
                .getTestsByUserIdAndLevel(GOOD_USER_ID, Levels.A1, pageable));
    }

    @org.junit.jupiter.api.Test
    void startAllTimers() {
        testsService.startAllTimers();

        verify(timerRepository).findAll();
    }

    @org.junit.jupiter.api.Test
    void spendAttempt(){
        Mockito.when(testsRepository.findById(GOOD_TEST_ID)).thenReturn(Optional.of(test));

        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(GOOD_USER_ID);
            Mockito.when(test.getListeningAttempts()).thenReturn(3);

            testsService.spendAttempt(GOOD_TEST_ID);

            verify(testsRepository).updateListeningAttempts(2, GOOD_TEST_ID);
            verify(restrictionsService).checkStatus(test, Status.STARTED);
            verify(restrictionsService).checkOwnerIsCurrentUser(test, GOOD_USER_ID);

        }
    }
}
