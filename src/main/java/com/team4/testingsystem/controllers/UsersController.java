package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.UserDTO;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserTest;
import com.team4.testingsystem.enums.Role;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.services.UsersService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UsersController {
    private final UsersService usersService;
    private final TestsService testsService;

    @Autowired
    public UsersController(UsersService usersService, TestsService testsService) {
        this.usersService = usersService;
        this.testsService = testsService;
    }

    @ApiOperation("Get list of all coaches in the system")
    @GetMapping("/coaches")
    @Secured("ROLE_ADMIN")
    public List<UserDTO> getCoaches() {
        return convertToDTO(usersService.getUsersByRole(Role.COACH));
    }

    @ApiOperation("Get all users and their assigned tests (if exist)")
    @GetMapping("/employees")
    @Secured("ROLE_HR")
    public List<UserDTO> getAllUsersAndAssignedTests() {
        return testsService.getAllUsersAndAssignedTests().stream()
                .map(UserTest::toUserDTO)
                .collect(Collectors.toList());
    }

    @ApiOperation("Get users by name substring (ignoring case)")
    @GetMapping("/users")
    @Secured("ROLE_HR")
    public List<UserDTO> getAllUsersByNameLike(@RequestParam String name) {
        return convertToDTO(usersService.getByNameLike(name));
    }

    @ApiOperation(value = "Update current user's language")
    @PutMapping("/language")
    public void updateLanguage(@RequestParam String language) {
        usersService.updateLanguage(JwtTokenUtil.extractUserDetails().getId(), language);
    }

    private List<UserDTO> convertToDTO(List<User> users) {
        return users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }
}
