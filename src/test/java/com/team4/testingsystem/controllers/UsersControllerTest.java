package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.TestInfo;
import com.team4.testingsystem.dto.UserDTO;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserTest;
import com.team4.testingsystem.enums.Role;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.services.UsersService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {
    @Mock
    private UsersService usersService;

    @Mock
    private TestsService testsService;

    @InjectMocks
    private UsersController usersController;

    private User user;
    private com.team4.testingsystem.entities.Test test;

    @BeforeEach
    void init() {
        user = EntityCreatorUtil.createUser();
        test = EntityCreatorUtil.createTest(user, EntityCreatorUtil.createLevel());
    }

    @Test
    void getCoachesEmpty() {
        Mockito.when(usersService.getUsersByRole(Role.COACH)).thenReturn(Lists.emptyList());
        Assertions.assertTrue(usersController.getCoaches().isEmpty());
    }

    @Test
    void getCoachesSuccess() {
        Mockito.when(usersService.getUsersByRole(Role.COACH)).thenReturn(Lists.list(user));
        Assertions.assertEquals(Lists.list(new UserDTO(user)), usersController.getCoaches());
    }

    @Test
    void getAllUsersNoAssignedTest() {
        Mockito.when(testsService.getUsersWithAssignedTests())
                .thenReturn(Lists.list(new UserTest(user, null)));

        List<UserDTO> users = usersController.getAllUsers();
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(new UserDTO(user), users.get(0));
    }

    @Test
    void getAllUsersSuccess() {
        Mockito.when(testsService.getUsersWithAssignedTests())
                .thenReturn(Lists.list(new UserTest(user, test)));

        UserDTO expectedUserDTO = new UserDTO(user);
        expectedUserDTO.setAssignedTest(new TestInfo(test));

        List<UserDTO> users = usersController.getAllUsers();
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(expectedUserDTO, users.get(0));
    }

    @Test
    void updateLanguageSuccess() {
        CustomUserDetails userDetails = Mockito.mock(CustomUserDetails.class);
        Mockito.when(userDetails.getId()).thenReturn(1L);

        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);

            usersController.updateLanguage("rus");

            Mockito.verify(usersService).updateLanguage(1L, "rus");
        }
    }
}
