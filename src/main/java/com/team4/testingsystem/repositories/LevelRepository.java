package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Level;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelRepository extends CrudRepository<Level, Long> {
    Optional<Level> findById(Long id);

    Optional<Level> findByName(String name);
}
