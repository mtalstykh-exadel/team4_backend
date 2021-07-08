package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<UserEntity, Integer> {
    Optional<UserEntity> findByLogin(String login);
}
