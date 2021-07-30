package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Test;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TestsRepository extends CrudRepository<Test, Long> {

    @Override
    Optional<Test> findById(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.startedAt = ?1, t.status = 'STARTED' WHERE t.id = ?2")
    int start(LocalDateTime startDate, Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.finishedAt = ?1, t.status = 'FINISHED', t.evaluation = ?2  where t.id = ?3")
    int finish(LocalDateTime finishDate, int evaluation, Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.updatedAt = ?1, t.evaluation = ?2 where t.id = ?3")
    int updateEvaluation(LocalDateTime updateDate, int evaluation,  Long id);

    @Transactional
    @Modifying
    @Query (value = "DELETE FROM Test t where t.id = ?1")
    int removeById(Long id);
}
