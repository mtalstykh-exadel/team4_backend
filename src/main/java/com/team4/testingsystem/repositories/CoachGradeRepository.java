package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import javax.transaction.Transactional;

@Repository
public interface CoachGradeRepository extends CrudRepository<CoachGrade, Long> {
    Optional<CoachGrade> findByTestAndQuestion(Test test, Question question);

    Iterable<CoachGrade> findAllByTest(Test test);

    @Transactional
    @Modifying
    @Query(value = "UPDATE CoachGrade cg SET cg.grade = ?3 WHERE cg.test = ?1 AND cg.question = ?2")
    int updateGrade(Long testId, Long questionId, Integer grade);
}
