package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public interface TestsRepository extends CrudRepository<Test, Long> {

    @Query("select t from Test t where t.user = ?1 and t.isAvailable = true "
           + "order by case "
           + "when t.status = 'STARTED' then 'A' "
           + "when t.status = 'ASSIGNED' then 'B' ELSE 'C' end, "
           + "t.verifiedAt desc, t.deadline desc, t.assignedAt desc ")
    List<Test> getAllByUser(User user, Pageable pageable);

    @Query("select t from Test t where t.status in ?1 and t.isAvailable = true "
           + "order by case "
           + "when t.priority = 'High' then 'A' "
           + "when t.priority = 'Medium' then 'B' "
           + "when t.priority = 'Low' then 'C' ELSE 'D' end, "
           + "t.deadline asc, t.assignedAt desc ")
    List<Test> getByStatuses(Status[] statuses, Pageable pageable);

    @Query("select t from Test t "
            + "where t.user = ?1 "
            + "and t.isAvailable = true "
            + "and t.assignedAt is null "
            + "and t.startedAt >= ?2")
    List<Test> getSelfStartedByUserAfter(User user, Instant date);

    @Query("select t from Test t where t.coach.id = ?1 and t.status in ?2 and t.isAvailable = true "
           + "order by case "
           + "when t.priority = 'High' then 'A' "
           + "when t.priority = 'Medium' then 'B' "
           + "when t.priority = 'Low' then 'C' ELSE t.priority end, "
           + "t.deadline asc, t.assignedAt desc ")
    List<Test> getAllByAssignedCoachAndStatuses(Long coachId, Status[] status, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Test t set t.status = ?2 where t.id = ?1 and t.isAvailable = true")
    int updateStatusByTestId(Long testId, Status newStatus);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.startedAt = ?1, t.status = 'STARTED' "
            + "WHERE t.id = ?2 and t.isAvailable = true ")
    int start(Instant startDate, Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.assignedAt = null, t.deadline = null "
            + "WHERE t.id = ?1 and t.isAvailable = true")
    int deassign(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.completedAt = ?1, t.status = 'COMPLETED' "
            + "where t.id = ?2 and t.isAvailable = true")
    int finish(Instant finishDate, Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.verifiedAt = ?1, t.status = 'VERIFIED' "
            + "where t.id = ?2 and t.isAvailable = true")
    int coachSubmit(Instant updateDate, Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.isAvailable = false where t.id = ?1")
    int archiveById(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.coach = ?1 where t.id = ?2 and t.isAvailable = true")
    int assignCoach(User coach, Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Test t SET t.coach = null, t.status = 'COMPLETED' "
            + "where t.id = ?1 and t.isAvailable = true")
    int deassignCoach(Long id);
}
