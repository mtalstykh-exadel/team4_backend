package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.UserNotFoundException;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestsControllerTest {


    @Mock
    TestsService testsService;

    @Mock
    CustomUserDetails customUserDetails;

    @InjectMocks
    TestsController testsController;

    @Mock
    Test test;

    @org.junit.jupiter.api.Test
    void getAllSuccess(){
        List<Test> tests = new ArrayList<>();

        Mockito.when(testsService.getAll()).thenReturn(tests);

        Assertions.assertEquals(tests, testsController.getAll());
    }

    @org.junit.jupiter.api.Test
    void getByIdSuccess(){
        Mockito.when(testsService.getById(1L)).thenReturn(test);

        Assertions.assertEquals(test, testsController.getById(1L));
    }

    @org.junit.jupiter.api.Test
    void getByIdFail(){
        Mockito.when(testsService.getById(42L)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, ()-> testsController.getById(42L));
    }

    @org.junit.jupiter.api.Test
    void assignSuccess() {

       Mockito.when(testsService.createForUser(1L)).thenReturn(1L);

       Assertions.assertEquals(1L, testsController.assign(1L));

    }

    @org.junit.jupiter.api.Test
    void assignFail() {

        Mockito.when(testsService.createForUser(42L)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, ()-> testsController.assign(42L));

    }

    @org.junit.jupiter.api.Test
    void startNotAssignedSuccess() {

        try (MockedStatic<JwtTokenUtil> builderMockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {

            builderMockedStatic.when(JwtTokenUtil::extractUserDetails).thenReturn(customUserDetails);


            Mockito.when(customUserDetails.getId()).thenReturn(1L);

            Mockito.when(testsService.createForUser(1L)).thenReturn(1L);

            testsController.startNotAssigned();

            verify(testsService).start(1L);

            Assertions.assertEquals(1L, testsController.startNotAssigned());
        }
    }

    @org.junit.jupiter.api.Test
    void startAssignedSuccess() {

        testsController.startAssigned(1L);

        verify(testsService).start(1L);
    }

    @org.junit.jupiter.api.Test
    void startAssignedFail() {

        doThrow(TestNotFoundException.class).when(testsService).start(42L);

        Assertions.assertThrows(TestNotFoundException.class, ()-> testsController.startAssigned(42L));
    }

    @org.junit.jupiter.api.Test
    void finishSuccess() {

        testsController.finish(1L, 1);

        verify(testsService).finish(1L, 1);
    }

    @org.junit.jupiter.api.Test
    void finishFail() {

        doThrow(TestNotFoundException.class).when(testsService).finish(42L, 42);

        Assertions.assertThrows(TestNotFoundException.class, ()-> testsController.finish(42L, 42));
    }

    @org.junit.jupiter.api.Test
    void updateEvaluationSuccess() {

        testsController.updateEvaluation(1L, 1);

        verify(testsService).updateEvaluation(1L, 1);
    }

    @org.junit.jupiter.api.Test
    void updateEvaluationFail() {

        doThrow(TestNotFoundException.class).when(testsService).updateEvaluation(42L, 42);

        Assertions.assertThrows(TestNotFoundException.class, ()-> testsController.updateEvaluation(42L, 42));
    }

    @org.junit.jupiter.api.Test
    void removeByIdSuccess() {

        testsController.removeById(1L);

        verify(testsService).removeById(1L);
    }

    @org.junit.jupiter.api.Test
    void removeByIdFail() {

        doThrow(TestNotFoundException.class).when(testsService).removeById(42L);

        Assertions.assertThrows(TestNotFoundException.class, ()-> testsController.removeById(42L));
    }

}
