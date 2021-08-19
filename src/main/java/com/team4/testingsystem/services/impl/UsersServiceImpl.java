package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserRole;
import com.team4.testingsystem.enums.Role;
import com.team4.testingsystem.exceptions.UserNotFoundException;
import com.team4.testingsystem.exceptions.UserRoleNotFoundException;
import com.team4.testingsystem.repositories.UserRolesRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final UserRolesRepository userRolesRepository;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, UserRolesRepository userRolesRepository) {
        this.usersRepository = usersRepository;
        this.userRolesRepository = userRolesRepository;
    }

    @Override
    public User getUserById(Long id) {
        return usersRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        UserRole userRole = userRolesRepository.findByRoleName(role.getName())
                .orElseThrow(UserRoleNotFoundException::new);

        return usersRepository.findAllByRole(userRole);
    }

    @Override
    public List<User> getAll(Pageable pageable) {
        return usersRepository.getAll(pageable);
    }

    @Override
    public List<User> getByNameLike(String nameSubstring) {
        return usersRepository.findAllByNameContainsIgnoreCase(nameSubstring);
    }

    @Override
    public void updateLanguage(Long userId, String language) {
        if (usersRepository.setLanguageById(userId, language) == 0) {
            throw new UserNotFoundException();
        }
    }
}
