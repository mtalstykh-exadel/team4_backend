package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TestsServiceTest {

    @Mock
    Test test;

    @Mock
    UsersRepository usersRepository;

    @Mock
    TestsRepository testsRepository;

    @InjectMocks
    TestsService testsService;

    @org.junit.jupiter.api.Test
    void getByIdSuccess() {
        Mockito.when(testsRepository.findById(1L)).thenReturn(Optional.of(test));

        Assertions.assertEquals(test, testsService.getById(1L));

    }

    @org.junit.jupiter.api.Test
    void getByIdFail() {

        Assertions.assertThrows(NoSuchElementException.class, ()->testsService.getById(42L));

    }

    @org.junit.jupiter.api.Test
    void createWhenAssignFail(){

        //User doesn't exist
        Mockito.when(usersRepository.existsById(42L)).thenReturn(false);

        Assertions.assertThrows(NoSuchElementException.class, ()->testsService.createForUser(42L));
    }

    @org.junit.jupiter.api.Test
    void startSuccess() {

        Mockito.when(testsRepository.existsById(1L)).thenReturn(true);

        Assertions.assertDoesNotThrow((()->testsService.start(1L)));
    }

   @org.junit.jupiter.api.Test
    void startFail() {
        Assertions.assertThrows(NoSuchElementException.class,()->testsService.start(42L));
    }

    @org.junit.jupiter.api.Test
    void finishSuccess() {

        Mockito.when(testsRepository.existsById(1L)).thenReturn(true);

        Assertions.assertDoesNotThrow((()->testsService.finish(1L,1)));
    }

    @org.junit.jupiter.api.Test
    void finishFail() {

        Assertions.assertThrows(NoSuchElementException.class,()->testsService.finish(42L, 42));

    }

    @org.junit.jupiter.api.Test
    void updateEvaluationSuccess() {

        Mockito.when(testsRepository.existsById(1L)).thenReturn(true);

        Assertions.assertDoesNotThrow((()->testsService.updateEvaluation(1L,1)));
    }

    @org.junit.jupiter.api.Test
    void updateEvaluationFail() {

        Assertions.assertThrows(NoSuchElementException.class,()->testsService.updateEvaluation(42L, 42));

    }

    @org.junit.jupiter.api.Test
    void removeSuccess() {

        Assertions.assertDoesNotThrow((()->testsService.removeById(1L)));

    }


}