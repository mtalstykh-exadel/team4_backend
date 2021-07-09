package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer> {
    Optional<Question> getQuestionById(Long id);
}
