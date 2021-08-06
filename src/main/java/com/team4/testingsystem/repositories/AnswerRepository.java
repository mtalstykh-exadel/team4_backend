package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Answer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {

    @Transactional
    @Modifying
    @Query(value = "update Answer a set a.isCorrect = true where a.answerBody = ?1")
    void updateCorrect(String answerBody);
}
