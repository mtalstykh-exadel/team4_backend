package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.TestInfo;
import com.team4.testingsystem.dto.UserDTO;
import com.team4.testingsystem.enums.Role;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.services.UsersService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<UserDTO> getCoaches() {
        return usersService.getUsersByRole(Role.COACH).stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/employees")
    public List<UserDTO> getAllUsers() {
        return usersService.getAll().stream()
                .map(UserDTO::new)
                .peek(user -> user.setAssignedTest(findAssignedTest(user)))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Update current user's language")
    @PutMapping("/language")
    public void updateLanguage(@RequestParam String language) {
        usersService.updateLanguage(JwtTokenUtil.extractUserDetails().getId(), language);
    }

    private TestInfo findAssignedTest(UserDTO user) {
        return testsService.getByUserIdWithStatus(user.getId(), Status.ASSIGNED)
                .map(TestInfo::new)
                .orElse(null);
    }
}
