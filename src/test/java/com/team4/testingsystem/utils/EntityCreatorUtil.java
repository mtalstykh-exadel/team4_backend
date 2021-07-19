package com.team4.testingsystem.utils;

import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserRole;

public class EntityCreatorUtil {
    public static User createUser() {
        User user = new User();
        UserRole userRole = new UserRole();
        userRole.setId(1);
        userRole.setRoleName("role");
        user.setId(1L);
        user.setName("name");
        user.setLanguage("en");
        user.setLogin("login");
        user.setPassword("password");
        user.setRole(userRole);
        return user;
    }
}
