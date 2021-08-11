package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

public interface UsersRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);

    List<User> findAllByRole(UserRole role);

    List<User> findAll();

    List<User> findAllByNameContainsIgnoreCase(String nameLike);

    @Transactional
    @Modifying
    @Query(value = "UPDATE User user SET user.language = ?2 WHERE user.id = ?1")
    int setLanguageById(Long id, String language);
}
