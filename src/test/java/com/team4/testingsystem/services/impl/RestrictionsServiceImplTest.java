package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.IllegalGradeException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
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

@ExtendWith(MockitoExtension.class)
public class RestrictionsServiceImplTest {

    final long GOOD_USER_ID = 1L;

    @Mock
    Test test;

    @Mock
    Question question;

    @Mock
    Module module;

    @Mock
    User user;

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
    void checkTestContainsQuestion(){
        Mockito.when(test.getQuestions()).thenReturn(Lists.emptyList());

        Assertions.assertThrows(QuestionNotFoundException.class,
            () -> restrictionsService.checkTestContainsQuestion(test, question));
    }

    @org.junit.jupiter.api.Test
    void checkGradeIsCorrect(){

        Assertions.assertThrows(IllegalGradeException.class,
            () -> restrictionsService.checkGradeIsCorrect(-42));
    }

    @org.junit.jupiter.api.Test
    void checkModuleIsEssayOrSpeaking(){
        Mockito.when(question.getModule()).thenReturn(module);
        Mockito.when(module.getName()).thenReturn(Modules.GRAMMAR.getName());
        Assertions.assertThrows(AccessControlException.class,
            () -> restrictionsService.checkModuleIsEssayOrSpeaking(question));
    }

    @org.junit.jupiter.api.Test
    void checkCoachIsCurrentUser(){
        Mockito.when(test.getCoach()).thenReturn(user);
        Mockito.when(user.getId()).thenReturn(GOOD_USER_ID);

        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(GOOD_USER_ID + 1);

            Assertions.assertThrows(AccessControlException.class,
                () -> restrictionsService.checkCoachIsCurrentUser(test));
        }
    }
}
