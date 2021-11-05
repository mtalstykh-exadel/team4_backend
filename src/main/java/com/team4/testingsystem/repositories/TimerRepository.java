package com.team4.testingsystem.repositories;

import com.team4.testingsystem.model.entity.Timer;
import com.team4.testingsystem.enums.Status;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TimerRepository extends CrudRepository<Timer, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Timer t where t.test.id = ?1 and t.status = ?2")
    void deleteByTestIdAndStatus(Long testId, Status status);
}
