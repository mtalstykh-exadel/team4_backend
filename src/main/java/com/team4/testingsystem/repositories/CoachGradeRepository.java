package com.team4.testingsystem.repositories;

import com.team4.testingsystem.model.entity.CoachGrade;
import com.team4.testingsystem.model.entity.Test;
import com.team4.testingsystem.model.entity.TestQuestionID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CoachGradeRepository extends CrudRepository<CoachGrade, TestQuestionID> {
    Collection<CoachGrade> findAllById_Test(Test test);
}
