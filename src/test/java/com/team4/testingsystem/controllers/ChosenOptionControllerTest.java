package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.ChosenOptionConverter;
import com.team4.testingsystem.dto.ChosenOptionDTO;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ChosenOptionControllerTest {

    @Mock
    private ChosenOptionService chosenOptionService;

    @Mock
    private ChosenOptionConverter converter;

    @InjectMocks
    private ChosenOptionController chosenOptionController;

    private static final Long TEST_ID = 1L;
    private static final Long QUESTION_ID = 2L;

    private ChosenOption chosenOption;

    @BeforeEach
    void init() {
        chosenOption = EntityCreatorUtil.createChosenOption();
    }

    @Test
    void getById() {
        Mockito.when(chosenOptionService.getByTestAndQuestionId(TEST_ID, QUESTION_ID))
                .thenReturn(chosenOption);

        Assertions.assertEquals(new ChosenOptionDTO(chosenOption),
                chosenOptionController.getById(TEST_ID, QUESTION_ID));
    }

    @Test
    void getAllByTest() {
        com.team4.testingsystem.entities.Test test = chosenOption.getId().getTest();

        Mockito.when(chosenOptionService.getAllByTest(ArgumentMatchers.any()))
                .thenReturn(List.of(chosenOption));

        Assertions.assertEquals(List.of(new ChosenOptionDTO(chosenOption)),
                chosenOptionController.getAllByTest(test.getId()));
    }

    @Test
    void save() {
        ChosenOptionDTO chosenOptionDTO = new ChosenOptionDTO(chosenOption);
        Mockito.when(converter.convertToEntity(chosenOptionDTO)).thenReturn(chosenOption);

        chosenOptionController.save(chosenOptionDTO);
        Mockito.verify(chosenOptionService).save(chosenOption);
    }

    @Test
    void saveAll() {
        ChosenOptionDTO chosenOptionDTO = new ChosenOptionDTO(chosenOption);
        Mockito.when(converter.convertToEntity(chosenOptionDTO)).thenReturn(chosenOption);

        chosenOptionController.saveAll(List.of(chosenOptionDTO));
        Mockito.verify(chosenOptionService).saveAll(List.of(chosenOption));
    }
}
