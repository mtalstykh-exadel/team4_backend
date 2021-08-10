package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.exceptions.ChosenOptionBadRequestException;
import com.team4.testingsystem.exceptions.ChosenOptionNotFoundException;
import com.team4.testingsystem.repositories.ChosenOptionRepository;
import com.team4.testingsystem.services.ChosenOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.persistence.EntityNotFoundException;

@Service
public class ChosenOptionServiceImpl implements ChosenOptionService {

    private final ChosenOptionRepository chosenOptionRepository;

    @Autowired
    public ChosenOptionServiceImpl(ChosenOptionRepository chosenOptionRepository) {
        this.chosenOptionRepository = chosenOptionRepository;
    }

    @Override
    public ChosenOption getById(TestQuestionID testQuestionID) {
        return chosenOptionRepository.findById(testQuestionID)
                .orElseThrow(ChosenOptionNotFoundException::new);
    }

    @Override
    public List<ChosenOption> getChosenOptionByTest(Test id) {
        return chosenOptionRepository.findChosenOptionsById_Test(id);
    }

    @Override
    public void save(ChosenOption chosenOption) {
        try {
            chosenOptionRepository.save(chosenOption);
        } catch (EntityNotFoundException exception) {
            throw new ChosenOptionBadRequestException();
        }
    }

    @Override
    public void saveAll(List<ChosenOption> chosenOptions) {
        try {
            chosenOptionRepository.saveAll(chosenOptions);
        } catch (EntityNotFoundException exception) {
            throw new ChosenOptionBadRequestException();
        }
    }

}
