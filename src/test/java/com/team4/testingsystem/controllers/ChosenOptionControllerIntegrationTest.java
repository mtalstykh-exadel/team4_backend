package com.team4.testingsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.testingsystem.dto.ChosenOptionDTO;
import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.repositories.AnswerRepository;
import com.team4.testingsystem.repositories.ChosenOptionRepository;
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

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class ChosenOptionControllerIntegrationTest {


    private final MockMvc mockMvc;
    private User user;
    private CustomUserDetails userDetails;
    private CustomUserDetails userDetails1;
    private final ChosenOptionRepository chosenOptionRepository;
    private final TestsRepository testsRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UsersRepository usersRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChosenOptionControllerIntegrationTest(MockMvc mockMvc,
                                                 ChosenOptionRepository chosenOptionRepository,
                                                 TestsRepository testsRepository,
                                                 QuestionRepository questionRepository,
                                                 AnswerRepository answerRepository,
                                                 UsersRepository usersRepository,
                                                 ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.chosenOptionRepository = chosenOptionRepository;
        this.testsRepository = testsRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.usersRepository = usersRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void init() {
        user = usersRepository.findByLogin("rus_user@northsixty.com").orElseThrow();
        userDetails = new CustomUserDetails(user);
        userDetails1 = new CustomUserDetails(usersRepository.findByLogin("eng_user@northsixty.com").orElseThrow());

    }

    @AfterEach
    void destroy() {

        chosenOptionRepository.deleteAll();
        testsRepository.deleteAll();
        answerRepository.deleteAll();
        questionRepository.deleteAll();


    }


    @Test
    void saveAllSuccess() throws Exception {
        Level level = EntityCreatorUtil.createLevel();
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);
        Question question = EntityCreatorUtil.createQuestion();

        questionRepository.save(question);

        question = questionRepository.findAll().iterator().next();

        Answer answer = EntityCreatorUtil.createAnswer(question);

        answerRepository.save(answer);

        testsRepository.save(test);

        answer.setQuestion(question);

        question.setTests(List.of(test));

        question.setAnswers(List.of(answer));

        questionRepository.save(question);

        test.setQuestions(List.of(question));

        testsRepository.save(test);

        TestQuestionID testQuestionID = new TestQuestionID(test, question);

        ChosenOption chosenOption = new ChosenOption(testQuestionID, answer);

        ChosenOptionDTO chosenOptionDTO = new ChosenOptionDTO(chosenOption);

        mockMvc.perform(post("/chosen_option/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(chosenOptionDTO)))
                .with(user(userDetails)))
                .andExpect(status().isOk());

        ChosenOption chosenOption1 = chosenOptionRepository.findById(testQuestionID).orElseThrow();

        Assertions.assertEquals(chosenOption.getId().getTest().getId(), chosenOption1.getId().getTest().getId());

        Assertions.assertEquals(chosenOption.getId().getQuestion().getId(), chosenOption1.getId().getQuestion().getId());

        Assertions.assertEquals(chosenOption.getAnswer().getId(), chosenOption1.getAnswer().getId());
    }

    @Test
    void saveAllTestNotStarted() throws Exception {
        Level level = EntityCreatorUtil.createLevel();
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);
        Question question = EntityCreatorUtil.createQuestion();

        questionRepository.save(question);

        question = questionRepository.findAll().iterator().next();

        Answer answer = EntityCreatorUtil.createAnswer(question);

        answerRepository.save(answer);

        testsRepository.save(test);

        answer.setQuestion(question);

        question.setTests(List.of(test));

        question.setAnswers(List.of(answer));

        questionRepository.save(question);

        test.setQuestions(List.of(question));

        test.setStatus(Status.VERIFIED);

        testsRepository.save(test);

        TestQuestionID testQuestionID = new TestQuestionID(test, question);

        ChosenOption chosenOption = new ChosenOption(testQuestionID, answer);

        ChosenOptionDTO chosenOptionDTO = new ChosenOptionDTO(chosenOption);

        mockMvc.perform(post("/chosen_option/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(chosenOptionDTO)))
                .with(user(userDetails)))
                .andExpect(status().isForbidden());

    }

    @Test
    void saveAllIncorrectUser() throws Exception {
        Level level = EntityCreatorUtil.createLevel();
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);
        Question question = EntityCreatorUtil.createQuestion();

        questionRepository.save(question);

        question = questionRepository.findAll().iterator().next();

        Answer answer = EntityCreatorUtil.createAnswer(question);

        answerRepository.save(answer);

        testsRepository.save(test);

        answer.setQuestion(question);

        question.setTests(List.of(test));

        question.setAnswers(List.of(answer));

        questionRepository.save(question);

        test.setQuestions(List.of(question));

        testsRepository.save(test);

        TestQuestionID testQuestionID = new TestQuestionID(test, question);

        ChosenOption chosenOption = new ChosenOption(testQuestionID, answer);

        ChosenOptionDTO chosenOptionDTO = new ChosenOptionDTO(chosenOption);

        mockMvc.perform(post("/chosen_option/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(chosenOptionDTO)))
                .with(user(userDetails1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void saveAllNoQuestionInTest() throws Exception {
        Level level = EntityCreatorUtil.createLevel();
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);
        Question question = EntityCreatorUtil.createQuestion();

        questionRepository.save(question);

        question = questionRepository.findAll().iterator().next();

        Answer answer = EntityCreatorUtil.createAnswer(question);
        answerRepository.save(answer);

        testsRepository.save(test);

        answer.setQuestion(question);

        question.setAnswers(List.of(answer));

        questionRepository.save(question);

        testsRepository.save(test);

        TestQuestionID testQuestionID = new TestQuestionID(test, question);

        ChosenOption chosenOption = new ChosenOption(testQuestionID, answer);

        ChosenOptionDTO chosenOptionDTO = new ChosenOptionDTO(chosenOption);

        mockMvc.perform(post("/chosen_option/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(chosenOptionDTO)))
                .with(user(userDetails)))
                .andExpect(status().isNotFound());
    }

}
