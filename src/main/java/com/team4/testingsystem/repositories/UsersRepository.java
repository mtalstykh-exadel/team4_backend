package com.team4.testingsystem.repositories;

import com.team4.testingsystem.model.entity.User;
import com.team4.testingsystem.model.entity.UserRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);

    List<User> findAllByRole(UserRole role);

    @Query("select u from User u "
           + "order by u.name")
    List<User> getAll(Pageable pageable);

    List<User> findAllByNameContainsIgnoreCaseOrderByName(String nameSubstring, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE User user SET user.language = ?2 WHERE user.id = ?1")
    int setLanguageById(Long id, String language);
}
