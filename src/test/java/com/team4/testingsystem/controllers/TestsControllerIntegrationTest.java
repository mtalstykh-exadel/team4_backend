package com.team4.testingsystem.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.testingsystem.dto.TestInfo;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.repositories.LevelRepository;
import com.team4.testingsystem.repositories.ModuleRepository;
import com.team4.testingsystem.repositories.NotificationRepository;
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
    private final String page = "0";
    private final String count = "10";

    private final MockMvc mockMvc;

    private final LevelRepository levelRepository;
    private final ModuleRepository moduleRepository;
    private final TestsRepository testsRepository;
    private final UsersRepository usersRepository;
    private final ContentFilesRepository contentFilesRepository;
    private final QuestionRepository questionRepository;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    private User user;
    private User coach;
    private Level level;

    private CustomUserDetails userDetails;
    private CustomUserDetails hrDetails;
    private CustomUserDetails coachDetails;
    private CustomUserDetails adminDetails;

    @Autowired
    TestsControllerIntegrationTest(MockMvc mockMvc,
                                   LevelRepository levelRepository,
                                   ModuleRepository moduleRepository,
                                   TestsRepository testsRepository,
                                   UsersRepository userRepository,
                                   ContentFilesRepository contentFilesRepository,
                                   QuestionRepository questionRepository,
                                   NotificationRepository notificationRepository,
                                   ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.levelRepository = levelRepository;
        this.moduleRepository = moduleRepository;
        this.testsRepository = testsRepository;
        this.usersRepository = userRepository;
        this.contentFilesRepository = contentFilesRepository;
        this.questionRepository = questionRepository;
        this.notificationRepository = notificationRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void init() {
        user = usersRepository.findByLogin("rus_user@northsixty.com").orElseThrow();
        userDetails = new CustomUserDetails(user);
        hrDetails = new CustomUserDetails(usersRepository.findByLogin("rus_hr@northsixty.com").orElseThrow());
        coach = usersRepository.findByLogin("rus_coach@northsixty.com").orElseThrow();
        coachDetails = new CustomUserDetails(coach);
        adminDetails = new CustomUserDetails(usersRepository.findByLogin("rus_admin@northsixty.com").orElseThrow());

        testsRepository.deleteAll();
        level = levelRepository.findByName(Levels.A1.name()).orElseThrow();
    }

    @AfterEach
    void destroy() {
        notificationRepository.deleteAll();
        testsRepository.deleteAll();
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
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(hrDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<TestInfo> tests = objectMapper.readValue(response, new TypeReference<>() {
        });

        Assertions.assertEquals(1, tests.size());
        Assertions.assertEquals(test.getId(), tests.get(0).getTestId());

        contentFile.setQuestions(null);
        contentFilesRepository.save(contentFile);
    }

    @Test
    void getUsersTestsUser() throws Exception {
        mockMvc.perform(get("/tests/history/{userId}", BAD_USER_ID)
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(userDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUsersTestsCoach() throws Exception {
        mockMvc.perform(get("/tests/history/{userId}", BAD_USER_ID)
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(coachDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUsersTestsAdmin() throws Exception {
        mockMvc.perform(get("/tests/history/{userId}", BAD_USER_ID)
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(adminDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUnverifiedTestsForCurrentCoachSuccess() throws Exception {
        Module listeningModule = moduleRepository.findByName(Modules.LISTENING.getName()).orElseThrow();

        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);
        test.setCoach(coach);
        test.setStatus(Status.COMPLETED);
        testsRepository.save(test);

        Question question = EntityCreatorUtil.createQuestion();
        question.setId(null);
        question.setModule(listeningModule);

        ContentFile contentFile = new ContentFile("url", "topic", Lists.list(question));
        contentFilesRepository.save(contentFile);

        test.setQuestions(Lists.list(question));
        testsRepository.save(test);

        MvcResult mvcResult = mockMvc.perform(get("/tests/unverified_assigned")
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(coachDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<TestInfo> testInfos = objectMapper.readValue(response, new TypeReference<>() {
        });

        Assertions.assertEquals(1, testInfos.size());
        Assertions.assertEquals(test.getId(), testInfos.get(0).getTestId());

        contentFile.setQuestions(null);
        contentFilesRepository.save(contentFile);
    }

    @Test
    void getUnverifiedTestsForCurrentCoachUser() throws Exception {
        mockMvc.perform(get("/tests/unverified_assigned")
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(userDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUnverifiedTestsForCurrentCoachHr() throws Exception {
        mockMvc.perform(get("/tests/unverified_assigned")
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(hrDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUnverifiedTestsForCurrentCoachAdmin() throws Exception {
        mockMvc.perform(get("/tests/unverified_assigned")
                .param("pageNumb", page)
                .param("pageSize", count)
                .with(user(adminDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void assignCoachSuccess() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);
        test.setStatus(Status.COMPLETED);
        testsRepository.save(test);

        User coach = usersRepository.findByLogin("rus_coach@northsixty.com").orElseThrow();

        long coachId = coach.getId();
        long testId = test.getId();

        mockMvc.perform(post("/tests/assign_coach/{testId}", testId)
                .param("coachId", String.valueOf(coachId))
                .with(user(adminDetails)))
                .andExpect(status().isOk());

        Optional<com.team4.testingsystem.entities.Test> updatedTest = testsRepository.findById(testId);

        Assertions.assertTrue(updatedTest.isPresent());
        Assertions.assertEquals(coachId, updatedTest.get().getCoach().getId());
    }

    @Test
    void assignCoachFailUserNotFound() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);

        test.setStatus(Status.COMPLETED);
        testsRepository.save(test);

        long userId = BAD_USER_ID;
        long testId = test.getId();

        mockMvc.perform(post("/tests/assign_coach/{testId}", testId)
                .param("coachId", String.valueOf(userId))
                .with(user(adminDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void assignCoachFailTestNotFound() throws Exception {
        long userId = user.getId();
        long testId = BAD_TEST_ID;

        mockMvc.perform(post("/tests/assign_coach/{testId}", testId)
                .param("coachId", String.valueOf(userId))
                .with(user(adminDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void assignCoachFailSelfAssignment() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);

        test.setStatus(Status.COMPLETED);

        testsRepository.save(test);

        long userId = user.getId();
        long testId = test.getId();

        mockMvc.perform(post("/tests/assign_coach/{testId}", testId)
                .param("coachId", String.valueOf(userId))
                .with(user(adminDetails)))
                .andExpect(status().isConflict());
    }

    @Test
    void assignCoachUser() throws Exception {
        mockMvc.perform(post("/tests/assign_coach/{testId}", BAD_TEST_ID)
                .param("coachId", String.valueOf(BAD_USER_ID))
                .with(user(userDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void assignCoachHr() throws Exception {
        mockMvc.perform(post("/tests/assign_coach/{testId}", BAD_TEST_ID)
                .param("coachId", String.valueOf(BAD_USER_ID))
                .with(user(hrDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void assignCoachCoach() throws Exception {
        mockMvc.perform(post("/tests/assign_coach/{testId}", BAD_TEST_ID)
                .param("coachId", String.valueOf(BAD_USER_ID))
                .with(user(coachDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deassignCoachSuccess() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);

        test.setCoach(user);

        testsRepository.save(test);

        long testId = test.getId();

        mockMvc.perform(post("/tests/deassign_coach/{testId}", testId)
                .with(user(adminDetails)))
                .andExpect(status().isOk());

        Optional<com.team4.testingsystem.entities.Test> updatedTest = testsRepository.findById(testId);
        Assertions.assertTrue(updatedTest.isPresent());
        Assertions.assertNull(updatedTest.get().getCoach());
        Assertions.assertEquals(Status.COMPLETED.name(), updatedTest.get().getStatus().name());
    }

    @Test
    void deassignCoachFail() throws Exception {
        mockMvc.perform(post("/tests/deassign_coach/{testId}", BAD_TEST_ID)
                .with(user(adminDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deassignCoachUser() throws Exception {
        mockMvc.perform(post("/tests/deassign_coach/{testId}", BAD_TEST_ID)
                .with(user(userDetails)))

                .andExpect(status().isForbidden());
    }

    @Test
    void deassignCoachHr() throws Exception {
        mockMvc.perform(post("/tests/deassign_coach/{testId}", BAD_TEST_ID)
                .with(user(hrDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deassignCoachCoach() throws Exception {
        mockMvc.perform(post("/tests/deassign_coach/{testId}", BAD_TEST_ID)
                .with(user(coachDetails)))
                .andExpect(status().isForbidden());
    }
}
