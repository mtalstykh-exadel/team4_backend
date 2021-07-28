package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.ChosenOptionID;
import com.team4.testingsystem.exceptions.ChosenOptionBadRequestException;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.repositories.ChosenOptionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChosenOptionImplTest {

    @Mock
    ChosenOption chosenOption;

    @Mock
    ChosenOptionID chosenOptionID;

    @Mock
    ChosenOptionRepository chosenOptionRepository;

    @InjectMocks
    ChosenOptionServiceImpl chosenOptionService;



    @org.junit.jupiter.api.Test
    void getByIdSuccess() {

        Mockito.when(chosenOptionRepository.findById(chosenOptionID)).thenReturn(Optional.of(chosenOption));

        Assertions.assertEquals(chosenOption, chosenOptionService.getById(chosenOptionID));
    }

    @org.junit.jupiter.api.Test
    void getByIdFail() {

        Mockito.when(chosenOptionRepository.findById(chosenOptionID)).thenThrow(FileNotFoundException.class);

        Assertions.assertThrows(FileNotFoundException.class, () -> chosenOptionService.getById(chosenOptionID));
    }

    @org.junit.jupiter.api.Test
    void saveSuccess(){

        chosenOptionService.save(chosenOption);

        verify(chosenOptionRepository).save(any());
    }

    @org.junit.jupiter.api.Test
    void saveFail(){

        Mockito.when(chosenOptionRepository.save(chosenOption)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(ChosenOptionBadRequestException.class,
                () -> chosenOptionService.save(chosenOption));
    }

}
