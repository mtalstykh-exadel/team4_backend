package com.team4.testingsystem.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.repositories.LevelRepository;
import com.team4.testingsystem.repositories.ModuleRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.assertj.core.util.Lists;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class TestsControllerIntegrationTest {

    private final long BAD_USER_ID = 4242L;

    private final long BAD_TEST_ID = 42L;

    private final MockMvc mockMvc;

    private final LevelRepository levelRepository;
    private final ModuleRepository moduleRepository;
    private final TestsRepository testsRepository;
    private final UsersRepository usersRepository;
    private final ContentFilesRepository contentFilesRepository;
    private final QuestionRepository questionRepository;

    private final ObjectMapper objectMapper;

    private User user;
    private CustomUserDetails userDetails;
    private Level level;

    @Autowired
    TestsControllerIntegrationTest(MockMvc mockMvc,
                                   LevelRepository levelRepository,
                                   ModuleRepository moduleRepository,
                                   TestsRepository testsRepository,
                                   UsersRepository userRepository,
                                   ContentFilesRepository contentFilesRepository,
                                   QuestionRepository questionRepository,
                                   ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.levelRepository = levelRepository;
        this.moduleRepository = moduleRepository;
        this.testsRepository = testsRepository;
        this.usersRepository = userRepository;
        this.contentFilesRepository = contentFilesRepository;
        this.questionRepository = questionRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void init() {
        user = usersRepository.findByLogin("rus_user@northsixty.com").orElseThrow();
        userDetails = new CustomUserDetails(user);
        testsRepository.deleteAll();
        level = levelRepository.findByName(Levels.A1.name()).orElseThrow();
    }

    @AfterEach
    void destroy() {
        contentFilesRepository.deleteAll();
        testsRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Test
    void getUsersTestsSuccess() throws Exception {
        Module listeningModule = moduleRepository.findByName(Modules.LISTENING.getName()).orElseThrow();

        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);
        testsRepository.save(test);

        Question question = EntityCreatorUtil.createQuestion();
        question.setId(null);
        question.setModule(listeningModule);

        ContentFile contentFile = new ContentFile("url", "topic", Lists.list(question));
        contentFilesRepository.save(contentFile);

        test.setQuestions(Lists.list(question));
        testsRepository.save(test);

        long userId = user.getId();

        MvcResult mvcResult = mockMvc.perform(get("/tests/history/{userId}", userId)
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<TestDTO> tests = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertEquals(1, tests.size());
        Assertions.assertEquals(test.getId(), tests.get(0).getId());

        contentFile.setQuestions(null);
        contentFilesRepository.save(contentFile);
    }

    @Test
    void getUsersTestsFailUserNotFound() throws Exception {
        mockMvc.perform(get("/tests/history/{userId}", BAD_USER_ID)
                .with(user(userDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void assignCoachSuccess() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);
        testsRepository.save(test);

        User coach = usersRepository.findByLogin("rus_coach@northsixty.com").orElseThrow();

        long coachId = coach.getId();
        long testId = test.getId();

        mockMvc.perform(post("/tests/assign_coach/{testId}", testId)
                .param("coachId", String.valueOf(coachId))
                .with(user(userDetails)))
                .andExpect(status().isOk());

        Optional<com.team4.testingsystem.entities.Test> updatedTest = testsRepository.findById(testId);

        Assertions.assertTrue(updatedTest.isPresent());
        Assertions.assertEquals(coachId, updatedTest.get().getCoach().getId());
    }

    @Test
    void assignCoachFailUserNotFound() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);
        testsRepository.save(test);

        long userId = BAD_USER_ID;
        long testId = test.getId();

        mockMvc.perform(post("/tests/assign_coach/{testId}", testId)
                .param("coachId", String.valueOf(userId))
                .with(user(userDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void assignCoachFailTestNotFound() throws Exception {
        long userId = user.getId();
        long testId = BAD_TEST_ID;

        mockMvc.perform(post("/tests/assign_coach/{testId}", testId)
                .param("coachId", String.valueOf(userId))
                .with(user(userDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void assignCoachFailSelfAssignment() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);
        testsRepository.save(test);

        long userId = user.getId();
        long testId = test.getId();

        mockMvc.perform(post("/tests/assign_coach/{testId}", testId)
                .param("coachId", String.valueOf(userId))
                .with(user(userDetails)))
                .andExpect(status().isConflict());
    }

    @Test
    void deassignCoachSuccess() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);

        test.setCoach(user);

        testsRepository.save(test);

        long testId = test.getId();

        mockMvc.perform(post("/tests/deassign_coach/{testId}", testId)
                .with(user(userDetails)))
                .andExpect(status().isOk());

        Optional<com.team4.testingsystem.entities.Test> updatedTest = testsRepository.findById(testId);
        Assertions.assertTrue(updatedTest.isPresent());
        Assertions.assertNull(updatedTest.get().getCoach());
    }

    @Test
    void deassignCoachFail() throws Exception {
        mockMvc.perform(post("/tests/deassign_coach/{testId}", BAD_TEST_ID)
                .with(user(userDetails)))
                .andExpect(status().isNotFound());
    }
}
