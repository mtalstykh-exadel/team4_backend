package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.UserDTO;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Role;
import com.team4.testingsystem.services.UsersService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {
    @Mock
    private UsersService usersService;

    @InjectMocks
    private UsersController usersController;

    @Test
    void getCoachesEmpty() {
        Mockito.when(usersService.getUsersByRole(Role.COACH)).thenReturn(Lists.emptyList());
        Assertions.assertTrue(usersController.getCoaches().isEmpty());
    }

    @Test
    void getCoachesSuccess() {
        User user = EntityCreatorUtil.createUser();

        Mockito.when(usersService.getUsersByRole(Role.COACH)).thenReturn(Lists.list(user));
        Assertions.assertEquals(Lists.list(new UserDTO(user)), usersController.getCoaches());
    }
}
