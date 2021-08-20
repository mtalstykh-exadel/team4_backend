package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.CoachAnswer;
import com.team4.testingsystem.entities.TestQuestionID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachAnswerRepository extends CrudRepository<CoachAnswer, TestQuestionID> {
}
