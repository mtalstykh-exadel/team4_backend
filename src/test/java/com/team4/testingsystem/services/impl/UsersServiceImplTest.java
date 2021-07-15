package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.utils.EntityCreatorUtil;
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

    @InjectMocks
    private UsersServiceImpl usersService;

    @Test
    void getUserById() {
        User user = EntityCreatorUtil.createUser();
        Mockito.when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User result = usersService.getUserById(user.getId());
        Assertions.assertEquals(user, result);
    }

    @Test
    void userByIdNotFoundException() {
        Mockito.when(usersRepository.findById(1L)).thenThrow(new NotFoundException());
        Assertions.assertThrows(NotFoundException.class, () -> usersService.getUserById(1L));
    }
}