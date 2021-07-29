package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.ChosenOptionID;
import com.team4.testingsystem.entities.Test;

public interface ChosenOptionService {

    ChosenOption getById(ChosenOptionID chosenOptionID);

    Iterable<ChosenOption> getChosenOptionByTest(Test test);

    void save(ChosenOption chosenOption);

}
