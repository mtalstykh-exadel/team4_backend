package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRolesRepository extends CrudRepository<UserRole, Integer> {
    Optional<UserRole> findByRoleName(String roleName);
}
