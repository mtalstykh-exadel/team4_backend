package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;

public interface ChosenOptionService {

    ChosenOption getById(TestQuestionID testQuestionID);

    Iterable<ChosenOption> getChosenOptionByTest(Test test);

    void save(ChosenOption chosenOption);
}
