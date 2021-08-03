package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChosenOptionRepository extends CrudRepository<ChosenOption, TestQuestionID> {
    Iterable<ChosenOption> findById_Test(Test test);
}
