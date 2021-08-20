package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ModuleCoachAnswer;
import com.team4.testingsystem.entities.TestModuleID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleCoachAnswerRepository extends CrudRepository<ModuleCoachAnswer, TestModuleID> {
}
