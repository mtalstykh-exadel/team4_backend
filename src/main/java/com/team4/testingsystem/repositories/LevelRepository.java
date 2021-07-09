package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Level;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends CrudRepository<Level, Integer> {
}
