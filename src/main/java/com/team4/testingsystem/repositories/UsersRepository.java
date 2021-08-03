package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);

    List<User> findAllByRole(UserRole role);
}
