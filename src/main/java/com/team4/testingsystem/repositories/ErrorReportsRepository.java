package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface ErrorReportsRepository extends CrudRepository<ErrorReport, TestQuestionID> {

    Collection<ErrorReport> findAllById_Test(Test test);

    @Transactional
    @Modifying
    @Query(value = "DELETE from ErrorReport er WHERE er.id = ?1")
    int removeById(TestQuestionID testQuestionID);
}
