package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.TestEntity;
import org.springframework.data.repository.CrudRepository;

public interface TestsRepository extends CrudRepository<TestEntity, Long> {
}
