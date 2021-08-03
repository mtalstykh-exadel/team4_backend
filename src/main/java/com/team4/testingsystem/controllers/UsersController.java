package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.UserDTO;
import com.team4.testingsystem.enums.Role;
import com.team4.testingsystem.services.UsersService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @ApiOperation("Get list of all coaches in the system")
    @GetMapping("/coaches")
    public List<UserDTO> getCoaches() {
        return usersService.getUsersByRole(Role.COACH).stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }
}
