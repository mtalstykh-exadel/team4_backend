package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestsServiceTest {

    @Mock
    Test test;

    @Mock
    Test.Builder builder;

    @Mock
    User user;

    @Mock
    UsersRepository usersRepository;

    @Mock
    TestsRepository testsRepository;

    @InjectMocks
    TestsService testsService;


    @org.junit.jupiter.api.Test
    void getAllSuccess() {
        List<Test> tests = new ArrayList<>();
        Mockito.when(testsRepository.findAll()).thenReturn(tests);

        Assertions.assertEquals(tests, testsService.getAll());

    }

    @org.junit.jupiter.api.Test
    void getByIdSuccess() {

        Mockito.when(testsRepository.findById(1L)).thenReturn(Optional.of(test));

        Assertions.assertEquals(test, testsService.getById(1L));

    }

    @org.junit.jupiter.api.Test
    void getByIdFail() {

        //Test doesn't exist
        Mockito.when(testsRepository.findById(42L)).thenThrow(NoSuchElementException.class);

        Assertions.assertThrows(NoSuchElementException.class, () -> testsService.getById(42L));

    }

    @org.junit.jupiter.api.Test
    void createWhenAssignFail() {

        //User doesn't exist
        Mockito.when(usersRepository.findById(42L)).thenThrow(NoSuchElementException.class);

        Assertions.assertThrows(NoSuchElementException.class, () -> testsService.createForUser(42L));
    }

    @org.junit.jupiter.api.Test
    void createSuccess() {


        Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        MockedStatic<Test> testMockedStatic = Mockito.mockStatic(Test.class);
        testMockedStatic.when(Test::newBuilder).thenReturn(builder);

        Mockito.when(builder.setUser(any())).thenReturn(builder);
        Mockito.when(builder.setCreatedAt(any())).thenReturn(builder);
        Mockito.when(builder.setStatus(any())).thenReturn(builder);
        Mockito.when(builder.build()).thenReturn(test);

        Mockito.when(test.getId()).thenReturn(1L);


        Assertions.assertEquals(1L, testsService.createForUser(1L));

    }

    @org.junit.jupiter.api.Test
    void startSuccess() {

        Mockito.when(testsRepository.existsById(1L)).thenReturn(true);

        testsService.start(1L);

        verify(testsRepository, times(1)).start(any(LocalDateTime.class), anyLong());
    }

    @org.junit.jupiter.api.Test
    void startFail() {

        //Test doesn't exist
        Mockito.when(testsRepository.existsById(42L)).thenReturn(false);

        Assertions.assertThrows(NoSuchElementException.class, () -> testsService.start(42L));
    }

    @org.junit.jupiter.api.Test
    void finishSuccess() {

        Mockito.when(testsRepository.existsById(1L)).thenReturn(true);

        testsService.finish(1L, 1);

        verify(testsRepository, times(1)).finish(any(LocalDateTime.class), anyInt(), anyLong());
    }

    @org.junit.jupiter.api.Test
    void finishFail() {

        //Test doesn't exist
        Mockito.when(testsRepository.existsById(42L)).thenReturn(false);

        Assertions.assertThrows(NoSuchElementException.class, () -> testsService.finish(42L, 42));

    }

    @org.junit.jupiter.api.Test
    void updateEvaluationSuccess() {

        Mockito.when(testsRepository.existsById(1L)).thenReturn(true);

        testsService.updateEvaluation(1L, 1);

        verify(testsRepository, times(1)).updateEvaluation(any(LocalDateTime.class), anyInt(), anyLong());
    }

    @org.junit.jupiter.api.Test
    void updateEvaluationFail() {

        //Test doesn't exist
        Mockito.when(testsRepository.existsById(42L)).thenReturn(false);

        Assertions.assertThrows(NoSuchElementException.class, () -> testsService.updateEvaluation(42L, 42));

    }

    @org.junit.jupiter.api.Test
    void removeSuccess() {

        testsService.removeById(1L);

        verify(testsRepository, times(1)).deleteById(1L);

    }

    @org.junit.jupiter.api.Test
    void removeFail() {

        //Test doesn't exist
        doThrow(EmptyResultDataAccessException.class).when(testsRepository).deleteById(42L);

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> testsService.removeById(42L));

    }

}
