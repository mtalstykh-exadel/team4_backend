package com.team4.testingsystem.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.testingsystem.model.dto.CoachGradeDTO;
import com.team4.testingsystem.model.entity.CoachGrade;
import com.team4.testingsystem.model.entity.Level;
import com.team4.testingsystem.model.entity.Question;
import com.team4.testingsystem.model.entity.TestQuestionID;
import com.team4.testingsystem.model.entity.User;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.repositories.AnswerRepository;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.repositories.LevelRepository;
import com.team4.testingsystem.repositories.NotificationRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.repositories.TestsRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class CoachGradeControllerIntegrationTest {
    private final MockMvc mockMvc;

    private final LevelRepository levelRepository;
    private final UsersRepository usersRepository;
    private final QuestionRepository questionRepository;
    private final TestsRepository testsRepository;
    private final CoachGradeRepository gradeRepository;
    private final AnswerRepository answerRepository;
    private final ContentFilesRepository contentFilesRepository;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    private User user;
    private Level level;

    private CustomUserDetails userDetails;
    private CustomUserDetails hrDetails;
    private CustomUserDetails coachDetails;
    private CustomUserDetails adminDetails;

    @Autowired
    CoachGradeControllerIntegrationTest(MockMvc mockMvc,
                                        LevelRepository levelRepository,
                                        UsersRepository usersRepository,
                                        QuestionRepository questionRepository,
                                        TestsRepository testsRepository,
                                        CoachGradeRepository gradeRepository,
                                        AnswerRepository answerRepository,
                                        ContentFilesRepository contentFilesRepository,
                                        NotificationRepository notificationRepository,
                                        ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.levelRepository = levelRepository;
        this.usersRepository = usersRepository;
        this.questionRepository = questionRepository;
        this.testsRepository = testsRepository;
        this.gradeRepository = gradeRepository;
        this.answerRepository = answerRepository;
        this.contentFilesRepository = contentFilesRepository;
        this.notificationRepository = notificationRepository;

        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void init() {
        answerRepository.deleteAll();

        user = usersRepository.findByLogin("rus_coach@northsixty.com").orElseThrow();
        userDetails = new CustomUserDetails(usersRepository.findByLogin("rus_user@northsixty.com").orElseThrow());
        hrDetails = new CustomUserDetails(usersRepository.findByLogin("rus_hr@northsixty.com").orElseThrow());
        coachDetails = new CustomUserDetails(user);
        adminDetails = new CustomUserDetails(usersRepository.findByLogin("rus_admin@northsixty.com").orElseThrow());

        level = levelRepository.findByName(Levels.A1.name()).orElseThrow();
    }

    @AfterEach
    void destroy() {
        notificationRepository.deleteAll();
        gradeRepository.deleteAll();
        contentFilesRepository.deleteAll();
        answerRepository.deleteAll();
        testsRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Test
    void getGradesTestNotFound() throws Exception {
        mockMvc.perform(get("/grades/101")
                .with(user(coachDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getGradesEmptyList() throws Exception {
        com.team4.testingsystem.model.entity.Test test = EntityCreatorUtil.createTest(user, level);
        testsRepository.save(test);

        testsRepository.assignCoach(user, test.getId());

        testsRepository.updateStatusByTestId(test.getId(), Status.IN_VERIFICATION);

        MvcResult mvcResult = mockMvc.perform(get("/grades/{testId}", test.getId())
                .with(user(coachDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<CoachGradeDTO> grades = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertTrue(grades.isEmpty());
    }

    @Test
    void getGradesOneGrade() throws Exception {
        com.team4.testingsystem.model.entity.Test test = EntityCreatorUtil.createTest(user, level);
        testsRepository.save(test);

        testsRepository.assignCoach(user, test.getId());

        testsRepository.updateStatusByTestId(test.getId(), Status.IN_VERIFICATION);

        Question question = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question);

        TestQuestionID testQuestionID = new TestQuestionID(test, question);

        CoachGrade grade = new CoachGrade(testQuestionID, 8, "Comment");
        gradeRepository.save(grade);

        MvcResult mvcResult = mockMvc.perform(get("/grades/{testId}", test.getId())
                .with(user(coachDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<CoachGradeDTO> grades = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertEquals(List.of(new CoachGradeDTO(grade)), grades);
    }

    @Test
    void getGradesTwoGrades() throws Exception {
        com.team4.testingsystem.model.entity.Test test = EntityCreatorUtil.createTest(user, level);
        testsRepository.save(test);

        testsRepository.assignCoach(user, test.getId());

        testsRepository.updateStatusByTestId(test.getId(), Status.IN_VERIFICATION);


        Question question1 = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question1);

        Question question2 = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question2);


        TestQuestionID testQuestionID1 = new TestQuestionID(test, question1);

        TestQuestionID testQuestionID2 = new TestQuestionID(test, question2);

        CoachGrade grade = new CoachGrade(testQuestionID1, 8, "Comment1");
        gradeRepository.save(grade);

        CoachGrade grade2 = new CoachGrade(testQuestionID2, 7, "Comment2");
        gradeRepository.save(grade2);

        MvcResult mvcResult = mockMvc.perform(get("/grades/{testId}", test.getId())
                .with(user(coachDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<CoachGradeDTO> grades = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertEquals(2, grades.size());
        Assertions.assertTrue(grades.contains(new CoachGradeDTO(grade)));
        Assertions.assertTrue(grades.contains(new CoachGradeDTO(grade2)));
    }

    @Test
    void getGradesUser() throws Exception {
        mockMvc.perform(get("/grades/1")
                .with(user(userDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getGradesHr() throws Exception {
        mockMvc.perform(get("/grades/1")
                .with(user(hrDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getGradesAdmin() throws Exception {
        mockMvc.perform(get("/grades/1")
                .with(user(adminDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void addGradeTestNotFound() throws Exception {
        CoachGradeDTO gradeDTO = CoachGradeDTO.builder()
                .testId(200L)
                .questionId(300L)
                .grade(10)
                .build();

        mockMvc.perform(post("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .with(user(coachDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void addGradeQuestionNotFound() throws Exception {
        com.team4.testingsystem.model.entity.Test test = EntityCreatorUtil.createTest(user, level);
        testsRepository.save(test);

        CoachGradeDTO gradeDTO = CoachGradeDTO.builder()
                .testId(test.getId())
                .questionId(301L)
                .grade(10)
                .build();

        mockMvc.perform(post("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .with(user(coachDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void addGradeSuccess() throws Exception {
        com.team4.testingsystem.model.entity.Test test = EntityCreatorUtil.createTest(user, level);

        Question question = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question);

        testsRepository.save(test);


        question.setTests(List.of(test));

        questionRepository.save(question);

        test.setQuestions(List.of(question));

        testsRepository.save(test);


        testsRepository.assignCoach(user, test.getId());

        testsRepository.updateStatusByTestId(test.getId(), Status.IN_VERIFICATION);

        TestQuestionID testQuestionID = new TestQuestionID(test, question);

        CoachGradeDTO gradeDTO = CoachGradeDTO.builder()
                .testId(test.getId())
                .questionId(question.getId())
                .grade(10)
                .comment("Comment")
                .build();

        mockMvc.perform(post("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .with(user(coachDetails)))
                .andExpect(status().isOk());

        Optional<CoachGrade> grade = gradeRepository.findById(testQuestionID);
        Assertions.assertTrue(grade.isPresent());
        Assertions.assertEquals(gradeDTO.getGrade(), grade.get().getGrade());
        Assertions.assertEquals(gradeDTO.getComment(), grade.get().getComment());
    }

    @Test
    void addGradeUser() throws Exception {
        mockMvc.perform(post("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .with(user(userDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void addGradeHr() throws Exception {
        mockMvc.perform(post("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .with(user(hrDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void addGradeAdmin() throws Exception {
        mockMvc.perform(post("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .with(user(adminDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateGradeSuccess() throws Exception {

        com.team4.testingsystem.model.entity.Test test = EntityCreatorUtil.createTest(user, level);

        Question question = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question);

        testsRepository.save(test);

        test.addQuestion(question);

        testsRepository.save(test);

        testsRepository.assignCoach(user, test.getId());

        testsRepository.updateStatusByTestId(test.getId(), Status.IN_VERIFICATION);


        CoachGradeDTO gradeDTO = CoachGradeDTO.builder()
                .testId(test.getId())
                .questionId(question.getId())
                .grade(10)
                .comment("Comment2")
                .build();

        TestQuestionID testQuestionID = new TestQuestionID(test, question);

        CoachGrade grade = new CoachGrade(testQuestionID, 2, "Comment1");
        gradeRepository.save(grade);

        mockMvc.perform(post("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .with(user(coachDetails)))
                .andExpect(status().isOk());

        Optional<CoachGrade> savedGrade = gradeRepository.findById(testQuestionID);
        Assertions.assertTrue(savedGrade.isPresent());
        Assertions.assertEquals(10, savedGrade.get().getGrade());
        Assertions.assertEquals("Comment2", savedGrade.get().getComment());
    }
}
