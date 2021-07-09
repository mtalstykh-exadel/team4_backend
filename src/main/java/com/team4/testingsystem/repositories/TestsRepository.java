package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Test;
import org.springframework.data.repository.CrudRepository;

public interface TestsRepository extends CrudRepository<Test, Long> {
}
