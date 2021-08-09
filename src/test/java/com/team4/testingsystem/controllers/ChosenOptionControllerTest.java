package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.repositories.ChosenOptionRepository;
import com.team4.testingsystem.services.ChosenOptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.verify;

public class ChosenOptionControllerTest {

    @Mock
    private ChosenOptionService chosenOptionService;

    @Mock
    private ChosenOptionRepository chosenOptionRepository;

    @Mock
    private ChosenOption chosenOption;

    @Mock
    TestQuestionID testQuestionID;

    @Mock
    private List<ChosenOption> chosenOptions;

    @InjectMocks
    private ChosenOptionController chosenOptionController;

    @Test
    void getById() {

        Mockito.when(chosenOptionRepository.findById(testQuestionID)).thenReturn(java.util.Optional.of(chosenOption));
        Mockito.when(chosenOptionService.getById(testQuestionID)).thenReturn(chosenOption);
        Assertions.assertEquals(chosenOption, chosenOptionController.getById(testQuestionID));
    }

    @Test
    void getAllByTest() {
        Mockito.when(chosenOptionService.getChosenOptionByTestId(1L)).thenReturn(chosenOptions);
        Assertions.assertEquals(chosenOption, chosenOptionController.getAllByTest(1L));
    }

    @Test
    void save() {
        chosenOptionController.save(chosenOption);
        verify(chosenOptionService).save(chosenOption);
    }

    @Test
    void saveAll() {
        chosenOptionController.saveAll(chosenOptions);
        verify(chosenOptionService).saveAll(chosenOptions);
    }
}
