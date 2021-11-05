package com.team4.testingsystem.services;

import com.team4.testingsystem.model.entity.User;
import com.team4.testingsystem.enums.Role;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsersService {
    User getUserById(Long id);

    List<User> getUsersByRole(Role role);

    List<User> getAll(Pageable pageable);

    List<User> getByNameLike(String nameSubstring, Pageable pageable);

    void updateLanguage(Long userId, String language);
}
