package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public User getUserById(Long id) {
        User user = usersRepository.findById(id).orElseThrow(NotFoundException::new);
        return user;
    }
}
