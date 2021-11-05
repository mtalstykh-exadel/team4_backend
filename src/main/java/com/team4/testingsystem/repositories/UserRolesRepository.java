package com.team4.testingsystem.repositories;

import com.team4.testingsystem.model.entity.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRolesRepository extends CrudRepository<UserRole, Integer> {
    Optional<UserRole> findByRoleName(String roleName);
}
