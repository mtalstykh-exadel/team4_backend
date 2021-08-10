package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChosenOptionRepository extends CrudRepository<ChosenOption, TestQuestionID> {
    @Query(value = "select co from ChosenOption co where co.id.test.id = ?1 and co.id.question.id = ?2")
    Optional<ChosenOption> findByTestAndQuestionId(Long testId, Long questionId);

    @Query(value = "select co from ChosenOption co where co.id.test = ?1")
    List<ChosenOption> findByTest(Test test);
}
