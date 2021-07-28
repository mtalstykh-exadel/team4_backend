package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.ChosenOptionID;

public interface ChosenOptionService {

    ChosenOption getById(ChosenOptionID chosenOptionID);

    void save(ChosenOption chosenOption);

}
