package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ModuleGrade;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestModuleID;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface ModuleGradesRepository extends CrudRepository <ModuleGrade, TestModuleID> {

    Collection<ModuleGrade> findAllById_Test(Test test);
}
