package com.team4.testingsystem.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.testingsystem.dto.UserDTO;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserRole;
import com.team4.testingsystem.enums.Role;
import com.team4.testingsystem.repositories.UserRolesRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class UsersControllerIntegrationTest {
    private final MockMvc mockMvc;

    private final UsersRepository usersRepository;
    private final UserRolesRepository userRolesRepository;

    private final ObjectMapper objectMapper;

    private CustomUserDetails userDetails;

    @Autowired
    UsersControllerIntegrationTest(MockMvc mockMvc,
                                   UsersRepository usersRepository,
                                   UserRolesRepository userRolesRepository,
                                   ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.usersRepository = usersRepository;
        this.userRolesRepository = userRolesRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void init() {
        User user = usersRepository.findByLogin("rus_user@northsixty.com").orElseThrow();
        userDetails = new CustomUserDetails(user);
    }

    @Test
    void getCoaches() throws Exception {
        Optional<UserRole> coachRole = userRolesRepository.findByRoleName(Role.COACH.getName());
        Assertions.assertTrue(coachRole.isPresent());

        final List<User> coaches = usersRepository.findAllByRole(coachRole.get());
        final List<UserDTO> coachDTOs = coaches.stream().map(UserDTO::new).collect(Collectors.toList());

        MvcResult mvcResult = mockMvc.perform(get("/users/coaches")
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        final List<UserDTO> userDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertEquals(coachDTOs, userDTOs);
    }
}
