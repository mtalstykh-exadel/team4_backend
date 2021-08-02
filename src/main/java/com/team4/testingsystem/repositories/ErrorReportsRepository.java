package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ErrorReportsRepository extends CrudRepository<ErrorReport, Long> {

    Optional<ErrorReport> findByTestAndQuestion(Test test, Question question);

    Collection <ErrorReport> findAllByTest(Test test);

    @Transactional
    @Modifying
    @Query(value = "UPDATE ErrorReport er SET er.reportBody = ?1 WHERE er.test = ?2 AND er.question = ?3")
    int changeReportBody(String reportBody, Test test, Question question);

    @Transactional
    @Modifying
    @Query(value = "DELETE from ErrorReport er WHERE er.test = ?1 AND er.question = ?2")
    int removeByTestAndQuestion(Test test, Question question);
}
