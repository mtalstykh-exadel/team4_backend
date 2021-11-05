package com.team4.testingsystem.repositories;

import com.team4.testingsystem.model.entity.Module;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Long> {
    Optional<Module> findById(Long id);

    Optional<Module> findByName(String name);
}
