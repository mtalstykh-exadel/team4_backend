package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.ChosenOptionID;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.repositories.AnswerRepository;
import com.team4.testingsystem.repositories.ChosenOptionRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.ChosenOptionService;
import org.springframework.beans.factory.annotation.Autowired;

public class ChosenOptionServiceImpl implements ChosenOptionService {

    private ChosenOptionRepository chosenOptionRepository;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;

    @Autowired
    public ChosenOptionServiceImpl(ChosenOptionRepository chosenOptionRepository,
                                    QuestionRepository questionRepository,
                                    AnswerRepository answerRepository) {
        this.chosenOptionRepository = chosenOptionRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public ChosenOption getById(ChosenOptionID chosenOptionID) {
        return chosenOptionRepository.findById(chosenOptionID)
                .orElseThrow(FileNotFoundException::new);
    }

    @Override
    public void save(ChosenOption chosenOption) {
        chosenOptionRepository.save(chosenOption);
    }

    @Override
    public void update(ChosenOption chosenOption, ChosenOptionID chosenOptionID) {
        if (chosenOptionRepository.update(chosenOption, chosenOptionID) == 0) {
            throw new FileNotFoundException();
        }
    }
}
