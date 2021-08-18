package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ChosenOption;

import java.util.List;

public interface ChosenOptionService {

    ChosenOption getByTestAndQuestionId(Long testId, Long questionId);

    List<ChosenOption> getAllByTestId(Long testId);

    void saveAll(List<ChosenOption> chosenOptions);
}
