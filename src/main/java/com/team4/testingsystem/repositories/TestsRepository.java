package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Status;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TestsRepository extends CrudRepository<Test, Long> {

    List<Test> getAllByUser(User user);

    @Query("select t from Test t where t.status in ?1")
    List<Test> getByStatuses(Status[] statuses);


    @Query("select t from Test t"
            + " where t.user = ?1 "
            + "and t.assignedAt is null "
            + "and t.startedAt >= ?2")
    List<Test> getSelfStartedByUserAfter(User user, LocalDateTime date);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.startedAt = ?1, t.status = 'STARTED' WHERE t.id = ?2")
    int start(LocalDateTime startDate, Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.completedAt = ?1, t.status = 'COMPLETED' where t.id = ?2")
    int finish(LocalDateTime finishDate, Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.verifiedAt = ?1 where t.id = ?2")
    int updateEvaluation(LocalDateTime updateDate, Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Test t where t.id = ?1")
    int removeById(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.coach = ?1 where t.id = ?2")
    int assignCoach(User coach, Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.coach = null where t.id = ?1")
    int deassignCoach(Long id);
}
