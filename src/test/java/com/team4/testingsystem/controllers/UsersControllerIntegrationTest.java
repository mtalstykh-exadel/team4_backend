package com.team4.testingsystem.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.testingsystem.dto.UserDTO;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserRole;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Role;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.repositories.LevelRepository;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.UserRolesRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.AfterEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class UsersControllerIntegrationTest {
    private final MockMvc mockMvc;

    private final UsersRepository usersRepository;
    private final UserRolesRepository userRolesRepository;
    private final LevelRepository levelRepository;
    private final TestsRepository testsRepository;

    private final ObjectMapper objectMapper;

    private CustomUserDetails userDetails;
    private CustomUserDetails hrDetails;
    private CustomUserDetails coachDetails;
    private CustomUserDetails adminDetails;

    private final String page = "0";
    private final String count = "10";

    @Autowired
    UsersControllerIntegrationTest(MockMvc mockMvc,
                                   UsersRepository usersRepository,
                                   UserRolesRepository userRolesRepository,
                                   LevelRepository levelRepository,
                                   TestsRepository testsRepository,
                                   ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.usersRepository = usersRepository;
        this.userRolesRepository = userRolesRepository;
        this.levelRepository = levelRepository;
        this.testsRepository = testsRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void init() {
        userDetails = new CustomUserDetails(usersRepository.findByLogin("rus_user@northsixty.com").orElseThrow());
        hrDetails = new CustomUserDetails(usersRepository.findByLogin("rus_hr@northsixty.com").orElseThrow());
        coachDetails = new CustomUserDetails(usersRepository.findByLogin("rus_coach@northsixty.com").orElseThrow());
        adminDetails = new CustomUserDetails(usersRepository.findByLogin("rus_admin@northsixty.com").orElseThrow());
    }

    @AfterEach
    void destroy() {
        testsRepository.deleteAll();
    }

    @Test
    void getCoaches() throws Exception {
        Optional<UserRole> coachRole = userRolesRepository.findByRoleName(Role.COACH.getName());
        Assertions.assertTrue(coachRole.isPresent());

        final List<User> coaches = usersRepository.findAllByRole(coachRole.get());
        final List<UserDTO> coachDTOs = coaches.stream().map(UserDTO::new).collect(Collectors.toList());

        MvcResult mvcResult = mockMvc.perform(get("/coaches")
                .with(user(adminDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        final List<UserDTO> userDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertEquals(coachDTOs, userDTOs);
    }

    @Test
    void getCoachesUser() throws Exception {
        mockMvc.perform(get("/coaches")
                .with(user(userDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getCoachesHr() throws Exception {
        mockMvc.perform(get("/coaches")
                .with(user(hrDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getCoachesCoach() throws Exception {
        mockMvc.perform(get("/coaches")
                .with(user(coachDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUsersNoAssignedTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees")
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(hrDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        final List<UserDTO> userDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        userDTOs.forEach(user -> Assertions.assertNull(user.getAssignedTest()));
    }
/*
    @Test
    void getAllUsersAssigned() throws Exception {
        Level level = levelRepository.findByName(Levels.A1.name()).orElseThrow();

        usersRepository.findAll().stream()
                .map(user -> EntityCreatorUtil.createTest(user, level))
                .peek(test -> test.setStatus(Status.ASSIGNED))
                .forEach(testsRepository::save);

        MvcResult mvcResult = mockMvc.perform(get("/employees")
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(hrDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        final List<UserDTO> userDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        userDTOs.forEach(user -> Assertions.assertNotNull(user.getAssignedTest()));
    }
*/
    @Test
    void getAllUsersAssignedUser() throws Exception {
        mockMvc.perform(get("/employees")
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(userDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUsersAssignedCoach() throws Exception {
        mockMvc.perform(get("/employees")
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(coachDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUsersAssignedAdmin() throws Exception {
        mockMvc.perform(get("/employees")
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(adminDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUsersByNameLikeExactName() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users")
                .with(user(hrDetails))
                .param("name", "Russian User"))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<UserDTO> userDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertEquals(1, userDTOs.size());
        Assertions.assertEquals("Russian User", userDTOs.get(0).getName());
    }

    @Test
    void getAllUsersByNameLikeUser() throws Exception {
        mockMvc.perform(get("/users?name=", "an U")
                .with(user(userDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUsersByNameLikeCoach() throws Exception {
        mockMvc.perform(get("/users?name=", "an U")
                .with(user(coachDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUsersByNameLikeAdmin() throws Exception {
        mockMvc.perform(get("/users?name=", "an U")
                .with(user(adminDetails)))
                .andExpect(status().isForbidden());
    }


    @Test
    void getAllUsersByNameLikeSubstring() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users")
                .with(user(hrDetails))
                .param("name", "an U"))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<UserDTO> userDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertEquals(1, userDTOs.size());
        Assertions.assertEquals("Russian User", userDTOs.get(0).getName());
    }

    @Test
    void getAllUsersByNameLikeSubstringIgnoreCase() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users")
                .with(user(hrDetails))
                .param("name", "An u"))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<UserDTO> userDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertEquals(1, userDTOs.size());
        Assertions.assertEquals("Russian User", userDTOs.get(0).getName());
    }

    @Test
    void updateLanguage() throws Exception {
        mockMvc.perform(put("/language")
                .with(user(userDetails))
                .param("language", "eng"))
                .andExpect(status().isOk());

        Optional<User> user = usersRepository.findById(userDetails.getId());

        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals("eng", user.get().getLanguage());
    }
}
