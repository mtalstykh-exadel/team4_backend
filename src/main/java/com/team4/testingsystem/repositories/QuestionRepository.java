package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer> {
    Optional<Question> findById(Long id);
    Optional<Question> updatePoints(Long id, int points);
    Optional<Question> updateAvailability(Long id, int isAvailable);
    Question save(Question question);
}
