package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.UserNotFoundException;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.services.LevelService;
import com.team4.testingsystem.services.UsersService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestsServiceImplTest {

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

    @InjectMocks
    TestsServiceImpl testsService;


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

        Mockito.when(testsRepository.findById(42L)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.getById(42L));

    }

    @org.junit.jupiter.api.Test
    void getByUserIdSuccess() {
        try (MockedStatic<User> mockUser = Mockito.mockStatic(User.class)) {
            User.Builder mockBuilder = Mockito.mock(User.Builder.class);
            Mockito.when(mockBuilder.id(1L)).thenReturn(mockBuilder);
            Mockito.when(mockBuilder.build()).thenReturn(user);

            mockUser.when(User::builder).thenReturn(mockBuilder);
            Mockito.when(testsRepository.getAllByUser(user)).thenReturn(Lists.emptyList());

            Assertions.assertEquals(Lists.emptyList(), testsService.getByUserId(1L));
        }
    }

    @org.junit.jupiter.api.Test
    void createWhenAssignFail() {
        Level level = EntityCreatorUtil.createLevel();
        Mockito.when(levelService.getLevelByName(level.getName())).thenReturn(level);
        Mockito.when(usersService.getUserById(42L)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(
                UserNotFoundException.class, () -> testsService.createForUser(42L, Levels.A1));
    }

    @org.junit.jupiter.api.Test
    void createSuccess() {
        Level level = EntityCreatorUtil.createLevel();
        Mockito.when(levelService.getLevelByName(level.getName())).thenReturn(level);
        Mockito.when(usersService.getUserById(1L)).thenReturn(user);


        try (MockedStatic<Test> builderMockedStatic = Mockito.mockStatic(Test.class)) {

            builderMockedStatic.when(Test::builder).thenReturn(builder);

            Mockito.when(builder.user(any())).thenReturn(builder);
            Mockito.when(builder.createdAt(any())).thenReturn(builder);
            Mockito.when(builder.status(any())).thenReturn(builder);
            Mockito.when(builder.level(any())).thenReturn(builder);
            Mockito.when(builder.build()).thenReturn(test);

            Mockito.when(test.getId()).thenReturn(1L);
            Assertions.assertEquals(1L, testsService.createForUser(1L, Levels.A1));
        }

    }

    @org.junit.jupiter.api.Test
    void startSuccess() {

        Mockito.when(testsRepository.start(any(), anyLong())).thenReturn(1);

        testsService.start(1L);

        verify(testsRepository).start(any(LocalDateTime.class), anyLong());

        Assertions.assertDoesNotThrow(() -> testsService.start(1L));
    }

    @org.junit.jupiter.api.Test
    void startFail() {

        Mockito.when(testsRepository.start(any(), anyLong())).thenReturn(0);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.start(42L));
    }

    @org.junit.jupiter.api.Test
    void finishSuccess() {

        Mockito.when(testsRepository.finish(any(), anyInt(), anyLong())).thenReturn(1);

        testsService.finish(1L, 1);

        verify(testsRepository).finish(any(LocalDateTime.class), anyInt(), anyLong());

        Assertions.assertDoesNotThrow(() -> testsService.finish(1L, 1));
    }

    @org.junit.jupiter.api.Test
    void finishFail() {

        Mockito.when(testsRepository.finish(any(), anyInt(), anyLong())).thenReturn(0);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.finish(42L, 42));

    }

    @org.junit.jupiter.api.Test
    void updateEvaluationSuccess() {

        Mockito.when(testsRepository.updateEvaluation(any(), anyInt(), anyLong())).thenReturn(1);

        testsService.updateEvaluation(1L, 1);

        verify(testsRepository).updateEvaluation(any(LocalDateTime.class), anyInt(), anyLong());

        Assertions.assertDoesNotThrow(() -> testsService.updateEvaluation(1L, 1));
    }

    @org.junit.jupiter.api.Test
    void updateEvaluationFail() {

        Mockito.when(testsRepository.updateEvaluation(any(), anyInt(), anyLong())).thenReturn(0);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> testsService.updateEvaluation(42L, 42));
    }

    @org.junit.jupiter.api.Test
    void removeSuccess() {

        Mockito.when(testsRepository.removeById(1L)).thenReturn(1);

        testsService.removeById(1L);

        verify(testsRepository).removeById(1L);

        Assertions.assertDoesNotThrow(() -> testsService.removeById(1L));

    }

    @org.junit.jupiter.api.Test
    void removeFail() {

        Mockito.when(testsRepository.removeById(42L)).thenReturn(0);

        Assertions.assertThrows(TestNotFoundException.class, () -> testsService.removeById(42L));

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
}
