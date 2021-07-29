package com.team4.testingsystem.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.testingsystem.dto.CoachGradeDTO;
import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.utils.EntityCreatorUtil;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class CoachGradeControllerIntegrationTest {
    private final MockMvc mockMvc;

    private final UsersRepository usersRepository;
    private final QuestionRepository questionRepository;
    private final TestsRepository testsRepository;
    private final CoachGradeRepository gradeRepository;

    private final ObjectMapper objectMapper;

    private User user;
    private CustomUserDetails userDetails;

    @Autowired
    public CoachGradeControllerIntegrationTest(MockMvc mockMvc,
                                               UsersRepository usersRepository,
                                               QuestionRepository questionRepository,
                                               TestsRepository testsRepository,
                                               CoachGradeRepository gradeRepository,
                                               ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.usersRepository = usersRepository;
        this.questionRepository = questionRepository;
        this.testsRepository = testsRepository;
        this.gradeRepository = gradeRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void init() {
        user = usersRepository.findByLogin("rus_user@northsixty.com").orElseThrow();
        userDetails = new CustomUserDetails(user);
    }

    @Test
    void getGrade() throws Exception {
        mockMvc.perform(get("/grades/100/100")
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user);
        testsRepository.save(test);

        mockMvc.perform(get("/grades/{testId}/100", test.getId())
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        Question question = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question);

        mockMvc.perform(get("/grades/{testId}/{questionId}", test.getId(), question.getId())
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        CoachGrade grade = new CoachGrade(test, question, 8);
        gradeRepository.save(grade);

        MvcResult mvcResult = mockMvc.perform(get("/grades/{testId}/{questionId}", test.getId(), question.getId())
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        CoachGradeDTO gradeDTO = objectMapper.readValue(response, CoachGradeDTO.class);

        Assertions.assertEquals(new CoachGradeDTO(grade), gradeDTO);

        gradeRepository.delete(grade);
        questionRepository.delete(question);
        testsRepository.delete(test);
    }

    @Test
    void getGrades() throws Exception {
        mockMvc.perform(get("/grades/100")
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user);
        testsRepository.save(test);

        MvcResult mvcResult = mockMvc.perform(get("/grades/{testId}", test.getId())
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<CoachGradeDTO> grades = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertTrue(grades.isEmpty());

        Question question = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question);

        CoachGrade grade = new CoachGrade(test, question, 8);
        gradeRepository.save(grade);

        mvcResult = mockMvc.perform(get("/grades/{testId}", test.getId())
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andReturn();

        response = mvcResult.getResponse().getContentAsString();
        grades = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertEquals(List.of(new CoachGradeDTO(grade)), grades);

        Question question2 = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question2);

        CoachGrade grade2 = new CoachGrade(test, question, 7);
        gradeRepository.save(grade2);

        mvcResult = mockMvc.perform(get("/grades/{testId}", test.getId())
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andReturn();

        response = mvcResult.getResponse().getContentAsString();
        grades = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertEquals(2, grades.size());
        Assertions.assertTrue(grades.contains(new CoachGradeDTO(grade)));
        Assertions.assertTrue(grades.contains(new CoachGradeDTO(grade2)));

        gradeRepository.delete(grade);
        gradeRepository.delete(grade2);
        questionRepository.delete(question);
        questionRepository.delete(question2);
        testsRepository.delete(test);
    }

    @Test
    void createGrade() throws Exception {
        CoachGradeDTO gradeDTO = new CoachGradeDTO();
        gradeDTO.setTestId(100L);
        gradeDTO.setQuestionId(100L);
        gradeDTO.setGrade(10);

        mockMvc.perform(post("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user);
        testsRepository.save(test);

        gradeDTO.setTestId(test.getId());

        mockMvc.perform(post("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        Question question = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question);

        gradeDTO.setQuestionId(question.getId());

        mockMvc.perform(post("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .with(user(userDetails)))
                .andExpect(status().isOk());

        Optional<CoachGrade> grade = gradeRepository.findByTestAndQuestion(test, question);
        Assertions.assertTrue(grade.isPresent());
        Assertions.assertEquals(gradeDTO.getGrade(), grade.get().getGrade());

        gradeDTO.setGrade(1);

        mockMvc.perform(post("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .with(user(userDetails)))
                .andExpect(status().isConflict());

        gradeRepository.delete(grade.get());
        questionRepository.delete(question);
        testsRepository.delete(test);
    }

    @Test
    void updateGrade() throws Exception {
        CoachGradeDTO gradeDTO = new CoachGradeDTO();
        gradeDTO.setTestId(100L);
        gradeDTO.setQuestionId(100L);
        gradeDTO.setGrade(10);

        mockMvc.perform(put("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user);
        testsRepository.save(test);

        gradeDTO.setTestId(test.getId());

        mockMvc.perform(put("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        Question question = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question);

        gradeDTO.setQuestionId(question.getId());

        mockMvc.perform(put("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .with(user(userDetails)))
                .andExpect(status().isNotFound());

        CoachGrade grade = new CoachGrade(test, question, 2);
        gradeRepository.save(grade);

        mockMvc.perform(put("/grades/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .with(user(userDetails)))
                .andExpect(status().isOk());

        Optional<CoachGrade> coachGrade = gradeRepository.findByTestAndQuestion(test, question);
        Assertions.assertTrue(coachGrade.isPresent());
        Assertions.assertEquals(gradeDTO.getGrade(), coachGrade.get().getGrade());

        gradeRepository.delete(coachGrade.get());
        questionRepository.delete(question);
        testsRepository.delete(test);
    }
}