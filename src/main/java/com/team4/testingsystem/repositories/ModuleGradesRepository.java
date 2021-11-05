package com.team4.testingsystem.repositories;

import com.team4.testingsystem.model.entity.ModuleGrade;
import com.team4.testingsystem.model.entity.Test;
import com.team4.testingsystem.model.entity.TestModuleID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ModuleGradesRepository extends CrudRepository<ModuleGrade, TestModuleID> {

    Collection<ModuleGrade> findAllById_Test(Test test);
}
