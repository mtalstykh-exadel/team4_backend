package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.TestQuestionID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChosenOptionRepository extends CrudRepository<ChosenOption, TestQuestionID> {

    @Query("select ch from ChosenOption ch where ch.id.test.id = ?1 ")
    List<ChosenOption> findAllByTestId(Long id);
}
