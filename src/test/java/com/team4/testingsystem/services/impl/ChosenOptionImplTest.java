package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.ChosenOptionID;
import com.team4.testingsystem.exceptions.ChosenOptionBadRequestException;
import com.team4.testingsystem.repositories.ChosenOptionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChosenOptionImplTest {

    @Mock
    ChosenOption chosenOption;

    @Mock
    ChosenOptionID chosenOptionID;

    @Mock
    ChosenOptionRepository chosenOptionRepository;

    @Mock
    com.team4.testingsystem.entities.Test test;

    @InjectMocks
    ChosenOptionServiceImpl chosenOptionService;



    @Test
    void getByIdSuccess() {

        Mockito.when(chosenOptionRepository.findById(chosenOptionID)).thenReturn(Optional.of(chosenOption));

        Assertions.assertEquals(chosenOption, chosenOptionService.getById(chosenOptionID));
    }

    @Test
    void getByIdFail() {

        Mockito.when(chosenOptionRepository.findById(chosenOptionID)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> chosenOptionService.getById(chosenOptionID));
    }

    @Test
    void getChosenOptionByTestSuccess() {

        ArrayList<ChosenOption> chOptions = new ArrayList<>();
        ChosenOption chOption1 = new ChosenOption();
        ChosenOption chOption2 = new ChosenOption();
        chOptions.add(chOption1);
        chOptions.add(chOption2);

        Mockito.when(chosenOptionRepository.findChosenOptionsByChosenOptionID_Test(test)).thenReturn(chOptions);

        Assertions.assertEquals(chOptions, chosenOptionService.getChosenOptionByTest(test));
    }

    @Test
    void getEmptyChosenOptionByTest() {

        ArrayList<ChosenOption> chOptions = new ArrayList<>();

        Mockito.when(chosenOptionRepository.findChosenOptionsByChosenOptionID_Test(test)).thenReturn(chOptions);

        Assertions.assertEquals(chOptions, chosenOptionService.getChosenOptionByTest(test));
    }

    @Test
    void saveSuccess(){

        chosenOptionService.save(chosenOption);

        verify(chosenOptionRepository).save(chosenOption);
    }

    @Test
    void saveFail(){

        Mockito.when(chosenOptionRepository.save(chosenOption)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(ChosenOptionBadRequestException.class,
                () -> chosenOptionService.save(chosenOption));
    }

}
