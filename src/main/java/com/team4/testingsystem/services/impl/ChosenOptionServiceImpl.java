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
    public Iterable<ChosenOption> getChosenOptionByTest(Test test) {
        return chosenOptionRepository.findById_Test(test);
    }

    @Override
    public void save(ChosenOption chosenOption) {
        try {
            chosenOptionRepository.save(chosenOption);
        } catch (EntityNotFoundException exception) {
            throw new ChosenOptionBadRequestException();
        }
    }

}
