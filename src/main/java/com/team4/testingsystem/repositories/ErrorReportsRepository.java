package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ErrorReport;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ErrorReportsRepository extends CrudRepository<ErrorReport, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE ErrorReport er SET er.reportBody = ?1 WHERE er.id = ?2")
    int changeReportBody(String reportBody, Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE from ErrorReport er WHERE er.id = ?1")
    int removeById(Long id);
}
