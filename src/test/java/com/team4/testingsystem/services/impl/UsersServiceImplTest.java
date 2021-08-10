package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserRole;
import com.team4.testingsystem.enums.Role;
import com.team4.testingsystem.exceptions.UserNotFoundException;
import com.team4.testingsystem.exceptions.UserRoleNotFoundException;
import com.team4.testingsystem.repositories.UserRolesRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {
    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UserRolesRepository userRolesRepository;

    @InjectMocks
    private UsersServiceImpl usersService;

    @Mock
    private UserRole userRole;

    @Mock
    private User user;

    @Test
    void getUserById() {
        User user = EntityCreatorUtil.createUser();
        Mockito.when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User result = usersService.getUserById(user.getId());
        Assertions.assertEquals(user, result);
    }

    @Test
    void userByIdNotFoundException() {
        Mockito.when(usersRepository.findById(1L)).thenThrow(UserNotFoundException.class);
        Assertions.assertThrows(UserNotFoundException.class, () -> usersService.getUserById(1L));
    }

    @Test
    void getUsersByRoleNotFound() {
        Mockito.when(userRolesRepository.findByRoleName(Role.USER.getName())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserRoleNotFoundException.class, () -> usersService.getUsersByRole(Role.USER));
    }

    @Test
    void getUsersByRoleEmpty() {
        Mockito.when(userRolesRepository.findByRoleName(Role.USER.getName())).thenReturn(Optional.of(userRole));
        Mockito.when(usersRepository.findAllByRole(userRole)).thenReturn(Lists.emptyList());

        Assertions.assertTrue(usersService.getUsersByRole(Role.USER).isEmpty());
    }

    @Test
    void getUserByRoleSuccess() {
        Mockito.when(userRolesRepository.findByRoleName(Role.USER.getName())).thenReturn(Optional.of(userRole));
        Mockito.when(usersRepository.findAllByRole(userRole)).thenReturn(Lists.list(user));

        Assertions.assertEquals(Lists.list(user), usersService.getUsersByRole(Role.USER));
    }

    @Test
    void getAllSuccess() {
        Mockito.when(usersRepository.findAll()).thenReturn(Lists.list(user));
        Assertions.assertEquals(Lists.list(user), usersService.getAll());
    }

    @Test
    void updateLanguageUserNotFound() {
        Mockito.when(usersRepository.setLanguageById(1L, "rus")).thenReturn(0);
        Assertions.assertThrows(UserNotFoundException.class, () -> usersService.updateLanguage(1L, "rus"));
    }

    @Test
    void updateLanguageSuccess() {
        Mockito.when(usersRepository.setLanguageById(1L, "rus")).thenReturn(1);
        Assertions.assertDoesNotThrow(() -> usersService.updateLanguage(1L, "rus"));
    }
}
