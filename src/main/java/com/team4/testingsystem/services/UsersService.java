package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.User;

import java.util.Optional;

public interface UsersService {
    User getUserById(Long id);
}
