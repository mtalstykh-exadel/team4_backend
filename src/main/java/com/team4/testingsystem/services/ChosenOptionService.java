package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.TestQuestionID;

import java.util.List;

public interface ChosenOptionService {

    ChosenOption getById(TestQuestionID testQuestionID);

    List<ChosenOption> getChosenOptionByTestId(Long id);

    void save(ChosenOption chosenOption);

    void saveAll(List<ChosenOption> chosenOptions);
}
