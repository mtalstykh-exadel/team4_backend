package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
