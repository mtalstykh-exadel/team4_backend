package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.AnswersAreBadException;
import com.team4.testingsystem.exceptions.AssignmentFailException;
import com.team4.testingsystem.exceptions.CoachAssignmentFailException;
import com.team4.testingsystem.exceptions.IllegalGradeException;
import com.team4.testingsystem.exceptions.NoAudioException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.QuestionOrTopicEditingException;
import com.team4.testingsystem.exceptions.TestAlreadyStartedException;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.AccessControlException;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RestrictionsServiceImplTest {

    final long GOOD_USER_ID = 1L;

    @Mock
    Test test;

    @Mock
    ContentFile contentFile;

    @Mock
    Stream<Answer> stream;

    @Mock
    Question question;

    @Mock
    List<Answer> answers;

    @Mock
    Module module;

    @Mock
    User user;

    @Mock
    TestsRepository testsRepository;

    @Mock
    CustomUserDetails userDetails;

    @InjectMocks
    RestrictionsServiceImpl restrictionsService;

    @org.junit.jupiter.api.Test
    void checkOwnerIsCurrentUser() {
        Mockito.when(test.getUser()).thenReturn(user);
        Mockito.when(user.getId()).thenReturn(GOOD_USER_ID);

        Assertions.assertThrows(AccessControlException.class,
                () -> restrictionsService.checkOwnerIsCurrentUser(test, GOOD_USER_ID + 1));
    }

    @org.junit.jupiter.api.Test
    void checkStatus() {
        Mockito.when(test.getStatus()).thenReturn(Status.COMPLETED);
        Assertions.assertThrows(AccessControlException.class,
                () -> restrictionsService.checkStatus(test, Status.STARTED));
    }

    @org.junit.jupiter.api.Test
    void checkTestContainsQuestion() {
        Mockito.when(test.getQuestions()).thenReturn(Lists.emptyList());

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> restrictionsService.checkTestContainsQuestion(test, question));
    }

    @org.junit.jupiter.api.Test
    void checkGradeIsCorrect() {
        Assertions.assertDoesNotThrow(() -> restrictionsService.checkGradeIsCorrect(0));
        Assertions.assertDoesNotThrow(() -> restrictionsService.checkGradeIsCorrect(10));
        Assertions.assertThrows(IllegalGradeException.class,
                () -> restrictionsService.checkGradeIsCorrect(-42));
    }

    @org.junit.jupiter.api.Test
    void checkModuleIsEssayOrSpeaking() {
        Mockito.when(question.getModule()).thenReturn(module);
        Mockito.when(module.getName()).thenReturn(Modules.GRAMMAR.getName());
        Assertions.assertThrows(AccessControlException.class,
                () -> restrictionsService.checkModuleIsEssayOrSpeaking(question));
    }

    @org.junit.jupiter.api.Test
    void checkCoachIsCurrentUser() {
        Mockito.when(test.getCoach()).thenReturn(user);
        Mockito.when(user.getId()).thenReturn(GOOD_USER_ID);

        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(GOOD_USER_ID + 1);

            Assertions.assertThrows(AccessControlException.class,
                    () -> restrictionsService.checkCoachIsCurrentUser(test));
        }
    }

    @org.junit.jupiter.api.Test
    void checkIsAssigned() {
        Mockito.when(test.getAssignedAt()).thenReturn(null);

        Assertions.assertThrows(AssignmentFailException.class,
                () -> restrictionsService.checkIsAssigned(test));
    }

    @org.junit.jupiter.api.Test
    void checkHasNoAssignedTests() {
        Mockito.when(testsRepository.hasAssignedTests(user)).thenReturn(true);

        Assertions.assertThrows(AssignmentFailException.class,
                () -> restrictionsService.checkHasNoAssignedTests(user));
    }

    @org.junit.jupiter.api.Test
    void checkNotSelfAssign() {
        Mockito.when(user.getId()).thenReturn(GOOD_USER_ID);

        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(GOOD_USER_ID);

            Assertions.assertThrows(AccessControlException.class,
                    () -> restrictionsService.checkNotSelfAssign(user));
        }
    }

    @org.junit.jupiter.api.Test
    void checkNotSelfDeassign() {

        Mockito.when(user.getId()).thenReturn(GOOD_USER_ID);

        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(GOOD_USER_ID);

            Assertions.assertThrows(AccessControlException.class,
                    () -> restrictionsService.checkNotSelfDeassign(user));
        }
    }

    @org.junit.jupiter.api.Test
    void checkHasNoStartedTests() {
        Mockito.when(testsRepository.hasStartedTests(GOOD_USER_ID)).thenReturn(true);

        Assertions.assertThrows(TestAlreadyStartedException.class,
                () -> restrictionsService.checkHasNoStartedTests(GOOD_USER_ID));
    }

    @org.junit.jupiter.api.Test
    void checkNotSelfAssignmentCoach() {
        Mockito.when(test.getUser()).thenReturn(user);

        Mockito.when(user.getId()).thenReturn(GOOD_USER_ID);

        Assertions.assertThrows(CoachAssignmentFailException.class,
                () -> restrictionsService.checkNotSelfAssignmentCoach(test, GOOD_USER_ID));

    }

    @org.junit.jupiter.api.Test
    void checkNotVerifiedForCoachDeassign() {
        Mockito.when(test.getStatus()).thenReturn(Status.VERIFIED);
        Assertions.assertThrows(CoachAssignmentFailException.class,
                () -> restrictionsService.checkNotVerifiedForCoachDeassign(test));
    }

    @org.junit.jupiter.api.Test
    void checkNotSelfAssignAdmin() {
        Mockito.when(test.getUser()).thenReturn(user);

        Mockito.when(user.getId()).thenReturn(GOOD_USER_ID);

        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(GOOD_USER_ID);

            Assertions.assertThrows(AccessControlException.class,
                    () -> restrictionsService.checkNotSelfAssignAdmin(test));

        }
    }

    @org.junit.jupiter.api.Test
    void checkNotSelfDeassignAdmin() {
        Mockito.when(test.getUser()).thenReturn(user);

        Mockito.when(user.getId()).thenReturn(GOOD_USER_ID);

        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(GOOD_USER_ID);

            Assertions.assertThrows(AccessControlException.class,
                    () -> restrictionsService.checkNotSelfDeassignAdmin(test));

        }
    }

    @org.junit.jupiter.api.Test
    void checkModuleIsNotListening() {
        Mockito.when(question.getModule()).thenReturn(module);

        Mockito.when(module.getName()).thenReturn(Modules.LISTENING.getName());

        Assertions.assertThrows(AccessControlException.class,
                () -> restrictionsService.checkModuleIsNotListening(question));
    }

    @org.junit.jupiter.api.Test
    void checkAnswersAreCorrectNotFourAnswers() {
        Mockito.when(answers.size()).thenReturn(5);

        Assertions.assertThrows(AnswersAreBadException.class,
                () -> restrictionsService.checkAnswersAreCorrect(answers));
    }

    @org.junit.jupiter.api.Test
    void checkAnswersAreCorrectNotOneCorrect(){

        Mockito.when(answers.size()).thenReturn(4);

        Mockito.when(answers.stream()).thenReturn(stream);

        Mockito.when(stream.filter(any())).thenReturn(stream);

        Mockito.when(stream.count()).thenReturn(0L);

        Assertions.assertThrows(AnswersAreBadException.class,
                () -> restrictionsService.checkAnswersAreCorrect(answers));
    }

    @org.junit.jupiter.api.Test
    void checkNotArchivedQuestion(){
        Mockito.when(question.isAvailable()).thenReturn(false);

        Assertions.assertThrows(QuestionOrTopicEditingException.class,
                () -> restrictionsService.checkNotArchivedQuestion(question));
    }


    @org.junit.jupiter.api.Test
    void checkNotArchivedContentFile(){
        Mockito.when(contentFile.isAvailable()).thenReturn(false);

        Assertions.assertThrows(QuestionOrTopicEditingException.class,
                () -> restrictionsService.checkNotArchivedContentFile(contentFile));
    }

    @org.junit.jupiter.api.Test
    void checkListeningHasAudio(){
        Mockito.when(contentFile.getUrl()).thenReturn(null);
        Assertions.assertThrows(NoAudioException.class,
                () -> restrictionsService.checkListeningHasAudio(contentFile));
    }
}
