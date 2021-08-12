package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.exceptions.ChosenOptionBadRequestException;
import com.team4.testingsystem.exceptions.ChosenOptionNotFoundException;
import com.team4.testingsystem.repositories.ChosenOptionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ChosenOptionServiceImplTest {

    @Mock
    ChosenOption chosenOption;

    @Mock
    com.team4.testingsystem.entities.Test test;

    @Mock
    ChosenOptionRepository chosenOptionRepository;

    @InjectMocks
    ChosenOptionServiceImpl chosenOptionService;

    private static final Long TEST_ID = 1L;
    private static final Long QUESTION_ID = 2L;

    @Test
    void getByIdSuccess() {
        Mockito.when(chosenOptionRepository.findByTestAndQuestionId(TEST_ID, QUESTION_ID))
                .thenReturn(Optional.of(chosenOption));

        Assertions.assertEquals(chosenOption, chosenOptionService.getByTestAndQuestionId(TEST_ID, QUESTION_ID));
    }

    @Test
    void getByIdFail() {
        Mockito.when(chosenOptionRepository.findByTestAndQuestionId(TEST_ID, QUESTION_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ChosenOptionNotFoundException.class,
                () -> chosenOptionService.getByTestAndQuestionId(TEST_ID, QUESTION_ID));
    }

    @Test
    void getChosenOptionByTestSuccess() {
        List<ChosenOption> chosenOptions = List.of(new ChosenOption(), new ChosenOption());

        Mockito.when(chosenOptionRepository.findByTest(test)).thenReturn(chosenOptions);

        Assertions.assertEquals(chosenOptions, chosenOptionService.getAllByTest(test));
    }

    @Test
    void getEmptyChosenOptionByTest() {
        List<ChosenOption> chosenOptions = List.of();

        Mockito.when(chosenOptionRepository.findByTest(test)).thenReturn(chosenOptions);

        Assertions.assertEquals(chosenOptions, chosenOptionService.getAllByTest(test));
    }

    @Test
    void saveSuccess(){
        chosenOptionService.save(chosenOption);

        Mockito.verify(chosenOptionRepository).save(chosenOption);
    }

    @Test
    void saveFail(){
        Mockito.when(chosenOptionRepository.save(chosenOption)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(ChosenOptionBadRequestException.class,
                () -> chosenOptionService.save(chosenOption));
    }

    @Test
    void saveAllSuccess(){
        List<ChosenOption> chosenOptions = List.of(new ChosenOption(), new ChosenOption());

        chosenOptionService.saveAll(chosenOptions);

        Mockito.verify(chosenOptionRepository).saveAll(chosenOptions);
    }
}
