package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.FileAnswer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileAnswerRepository extends CrudRepository<FileAnswer, Long> {
}