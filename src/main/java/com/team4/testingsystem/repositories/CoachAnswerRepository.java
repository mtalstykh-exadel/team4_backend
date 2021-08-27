package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.CoachAnswer;
import com.team4.testingsystem.entities.TestQuestionID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachAnswerRepository extends CrudRepository<CoachAnswer, TestQuestionID> {
    @Query(value = "select ca from CoachAnswer ca "
            + "where ca.id.test.id = ?1")
    List<CoachAnswer> findAllByTestId(Long testId);
}
