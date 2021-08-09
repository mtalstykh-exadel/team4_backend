package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.TestQuestionID;
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
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChosenOptionImplTest {

    @Mock
    ChosenOption chosenOption;

    @Mock
    TestQuestionID testQuestionID;

    @Mock
    ChosenOptionRepository chosenOptionRepository;

    @InjectMocks
    ChosenOptionServiceImpl chosenOptionService;



    @Test
    void getByIdSuccess() {

        Mockito.when(chosenOptionRepository.findById(testQuestionID)).thenReturn(Optional.of(chosenOption));

        Assertions.assertEquals(chosenOption, chosenOptionService.getById(testQuestionID));
    }

    @Test
    void getByIdFail() {

        Mockito.when(chosenOptionRepository.findById(testQuestionID)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> chosenOptionService.getById(testQuestionID));
    }

    @Test
    void getChosenOptionByTestSuccess() {

        ArrayList<ChosenOption> chOptions = new ArrayList<>();
        ChosenOption chOption1 = new ChosenOption();
        ChosenOption chOption2 = new ChosenOption();
        chOptions.add(chOption1);
        chOptions.add(chOption2);

        Mockito.when(chosenOptionRepository.findById(1L)).thenReturn(chOptions);

        Assertions.assertEquals(chOptions, chosenOptionService.getChosenOptionByTestId(1L));
    }

    @Test
    void getEmptyChosenOptionByTest() {

        ArrayList<ChosenOption> chOptions = new ArrayList<>();

        Mockito.when(chosenOptionRepository.findById(2L)).thenReturn(chOptions);

        Assertions.assertEquals(chOptions, chosenOptionService.getChosenOptionByTestId(2L));
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

    @Test
    void saveAllSuccess(){

        List<ChosenOption> chosenOptions = new ArrayList<>();
        chosenOptions.add(new ChosenOption());
        chosenOptions.add(new ChosenOption());
        chosenOptions.add(new ChosenOption());

        chosenOptionService.saveAll(chosenOptions);

        verify(chosenOptionRepository).saveAll(chosenOptions);
    }

}
