package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import javax.transaction.Transactional;

@Repository
public interface CoachGradeRepository extends CrudRepository<CoachGrade, Long> {
    Optional<CoachGrade> findByQuestion(Question question);

    @Transactional
    @Modifying
    @Query(value = "UPDATE CoachGrade cg SET cg.grade = ?2 WHERE cg.id = ?1")
    int updateGrade(Long id, Integer grade);
}
