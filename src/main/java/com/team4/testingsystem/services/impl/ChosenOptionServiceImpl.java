package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.exceptions.ChosenOptionBadRequestException;
import com.team4.testingsystem.exceptions.ChosenOptionNotFoundException;
import com.team4.testingsystem.repositories.ChosenOptionRepository;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.services.RestrictionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.persistence.EntityNotFoundException;

@Service
public class ChosenOptionServiceImpl implements ChosenOptionService {

    private final ChosenOptionRepository chosenOptionRepository;
    private final RestrictionsService restrictionsService;

    @Autowired
    public ChosenOptionServiceImpl(ChosenOptionRepository chosenOptionRepository,
                                   RestrictionsService restrictionsService) {
        this.chosenOptionRepository = chosenOptionRepository;
        this.restrictionsService = restrictionsService;
    }

    @Override
    public ChosenOption getByTestAndQuestionId(Long testId, Long questionId) {
        return chosenOptionRepository.findByTestAndQuestionId(testId, questionId)
                .orElseThrow(ChosenOptionNotFoundException::new);
    }

    @Override
    public List<ChosenOption> getAllByTestId(Long testId) {
        return chosenOptionRepository.findByTestId(testId);
    }

    @Override
    public void saveAll(List<ChosenOption> chosenOptions) {
        Test test;

        Question question;

        for (ChosenOption item : chosenOptions) {
            test = item.getId().getTest();
            question = item.getId().getQuestion();

            restrictionsService.checkOwnerIsCurrentUser(test);

            restrictionsService.checkStartedStatus(test);

            restrictionsService.checkTestContainsQuestion(test, question);
        }

        try {
            chosenOptionRepository.saveAll(chosenOptions);
        } catch (EntityNotFoundException exception) {
            throw new ChosenOptionBadRequestException();
        }
    }
}
