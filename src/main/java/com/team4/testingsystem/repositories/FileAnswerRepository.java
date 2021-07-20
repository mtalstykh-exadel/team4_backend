package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.FileAnswer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileAnswerRepository extends CrudRepository<FileAnswer, Long> {

    @Query(value = "select f.id from FileAnswer f where f.id = ?1")
    int removeById(long id);
}