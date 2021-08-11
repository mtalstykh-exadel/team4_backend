package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Role;

import java.util.List;

public interface UsersService {
    User getUserById(Long id);

    List<User> getUsersByRole(Role role);

    List<User> getAll();

    List<User> getByNameLike(String nameLike);

    void updateLanguage(Long userId, String language);
}
