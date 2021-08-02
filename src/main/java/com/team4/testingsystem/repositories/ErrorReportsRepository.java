package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.ErrorReportId;
import com.team4.testingsystem.entities.Test;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ErrorReportsRepository extends CrudRepository<ErrorReport, ErrorReportId> {

    Optional<ErrorReport> findById(ErrorReportId errorReportId);

    Collection<ErrorReport> findAllByErrorReportIdTest(Test test);

    @Transactional
    @Modifying
    @Query(value = "DELETE from ErrorReport er WHERE er.errorReportId = ?1")
    int removeById(ErrorReportId errorReportId);
}
