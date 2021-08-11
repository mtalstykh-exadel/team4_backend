package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.entities.TestQuestionID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileAnswerRepository extends CrudRepository<FileAnswer, TestQuestionID> {
    @Query(value = "select fa from FileAnswer fa where fa.id.test.id = ?1 and fa.id.question.id = ?2")
    Optional<FileAnswer> findByTestAndQuestionId(Long testId, Long questionId);
}
