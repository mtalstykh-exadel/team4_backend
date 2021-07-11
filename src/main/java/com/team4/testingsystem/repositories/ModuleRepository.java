package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Module;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Integer> {
    Optional<Module> findById(Long id);
}
