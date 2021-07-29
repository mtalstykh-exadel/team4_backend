package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.ChosenOptionID;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.exceptions.ChosenOptionNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChosenOptionRepository extends CrudRepository<ChosenOption, ChosenOptionID> {

    Iterable<ChosenOption> findChosenOptionsByTest(Test test) throws ChosenOptionNotFoundException;

}