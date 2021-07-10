package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.TestEntity;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TestsServiceTest {

private final String DATE_FOR_TESTS = "2020-01-01T10:00:00";

    @Mock
    User user;

    @Mock
    TestEntity testEntity;

    @Mock
    UsersRepository usersRepository;

    @Mock
    TestsRepository testsRepository;

    @InjectMocks
    TestsService testsService;

    @Test
    void successfulGetting() {

        Mockito.when(testsRepository.existsById(1L)).thenReturn(true);
        Assertions.assertNotNull(testsService.getTestById(1));

    }

    @Test
    void failedGetting() {

        //Test doesn't exist
        Mockito.when(testsRepository.existsById(42L)).thenReturn(false);

        Assertions.assertNull(testsService.getTestById(42));

    }

    @Test
    void successfulAddingTest() {

        Mockito.when(usersRepository.existsById(1L)).thenReturn(true);
        Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        Assertions.assertEquals(testsService.addNewTest(1,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS, 42),"Success");

    }

    @Test
    void failedAddingTest() {

        //User doesn't exist
        Mockito.when(usersRepository.existsById(1L)).thenReturn(false);

        Assertions.assertEquals(testsService.addNewTest(1,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS, 42),"Fail");

    }

    @Test
    void successfulUpdatingTest() {

        Mockito.when(usersRepository.existsById(1L)).thenReturn(true);

        Mockito.when(testsRepository.existsById(1L)).thenReturn(true);

        Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        Mockito.when(testsRepository.findById(1L)).thenReturn(Optional.ofNullable(testEntity));

        Assertions.assertEquals(testsService.updateTest(1, 1,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS, 42),"Success");

    }

    @Test
    void failedUpdatingTestUserFail() {


        Mockito.when(testsRepository.existsById(1L)).thenReturn(true);

        //New user doesn't exist
        Mockito.when(usersRepository.existsById(42L)).thenReturn(false);

        Assertions.assertEquals(testsService.updateTest(1,42,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS, 42),"Fail");

    }


    @Test
    void failedUpdatingTestTestFail() {

        //Test doesn't exist
        Mockito.when(testsRepository.existsById(42L)).thenReturn(false);


        Assertions.assertEquals(testsService.updateTest(42,1,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS,
                DATE_FOR_TESTS, 42),"Fail");

    }


    @Test
    void successfulRemoving() {

        Mockito.when(testsRepository.existsById(1L)).thenReturn(true);
        Assertions.assertEquals(testsService.removeTestById(1), "Success");
    }

    @Test
    void failedRemoving() {

        //Test doesn't exist
        Mockito.when(testsRepository.existsById(42L)).thenReturn(false);

        Assertions.assertEquals(testsService.removeTestById(42), "Fail");
    }



}
