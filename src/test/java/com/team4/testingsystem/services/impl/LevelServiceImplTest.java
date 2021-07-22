package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.exceptions.LevelNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.LevelRepository;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LevelServiceImplTest {

    @Mock
    private LevelRepository levelRepository;

    @InjectMocks
    private LevelServiceImpl levelService;

    @Test
    void getLevelById() {
        Level level = EntityCreatorUtil.createLevel();
        Mockito.when(levelRepository.findById(level.getId())).thenReturn(Optional.of(level));
        Level result = levelService.getLevelById(level.getId());
        Assertions.assertEquals(level, result);
    }

    @Test
    void levelByIdNotFoundException() {
        Mockito.when(levelRepository.findById(1L)).thenThrow(new LevelNotFoundException());
        Assertions.assertThrows(LevelNotFoundException.class, () -> levelService.getLevelById(1L));
    }

    @Test
    void getLevelByName() {
        Level level = EntityCreatorUtil.createLevel();
        Mockito.when(levelRepository.findByName(level.getName())).thenReturn(Optional.of(level));
        Level result = levelService.getLevelByName(level.getName());
        Assertions.assertEquals(level, result);
    }


    @Test
    void levelByNameNotFoundException() {
        Mockito.when(levelRepository.findByName("name")).thenThrow(new LevelNotFoundException());
        Assertions.assertThrows(LevelNotFoundException.class, () -> levelService.getLevelByName("name"));
    }

}