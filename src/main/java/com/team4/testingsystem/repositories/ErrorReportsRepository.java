package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.TestQuestionID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface ErrorReportsRepository extends CrudRepository<ErrorReport, TestQuestionID> {

    @Query(value = "select er from ErrorReport er where er.id.test.id = ?1")
    Collection<ErrorReport> findAllByTestId(Long testId);

    @Transactional
    @Modifying
    @Query(value = "DELETE from ErrorReport er WHERE er.id = ?1")
    int removeById(TestQuestionID testQuestionID);
}
