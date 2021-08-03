package com.team4.testingsystem.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.testingsystem.dto.ErrorReportDTO;
import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.repositories.ErrorReportsRepository;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class ErrorReportsControllerIntegrationTest {

    private final long BAD_TEST_ID = 42L;

    private final long BAD_QUESTION_ID = 42L;

    private final String GOOD_REPORT_BODY = "Good report body";
    private final String BAD_REPORT_BODY = "Bad report body";
    private final String NEW_REPORT_BODY = "New report body";

    private final MockMvc mockMvc;

    private final UsersRepository usersRepository;
    private final QuestionRepository questionRepository;
    private final TestsRepository testsRepository;
    private final ErrorReportsRepository errorReportsRepository;

    private final ObjectMapper objectMapper;

    private User user;
    private CustomUserDetails userDetails;

    @Autowired
    ErrorReportsControllerIntegrationTest(MockMvc mockMvc,
                                                 UsersRepository usersRepository,
                                                 QuestionRepository questionRepository,
                                                 TestsRepository testsRepository,
                                                 ErrorReportsRepository errorReportsRepository,
                                                 ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.usersRepository = usersRepository;
        this.questionRepository = questionRepository;
        this.testsRepository = testsRepository;
        this.errorReportsRepository = errorReportsRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void init() {
        user = usersRepository.findByLogin("rus_user@northsixty.com").orElseThrow();
        userDetails = new CustomUserDetails(user);
    }

    @AfterEach
    void destroy() {
        errorReportsRepository.deleteAll();
        questionRepository.deleteAll();
        testsRepository.deleteAll();
    }

    @Test
    void getReportsEmptyList() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user);
        testsRepository.save(test);

        Question question = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question);

        MvcResult mvcResult = mockMvc.perform(get("/error_reports/{testId}", test.getId())
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<ErrorReportDTO> reports = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertTrue(reports.isEmpty());
    }

    @Test
    void getReportsTwoElements() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user);
        testsRepository.save(test);


        Question question1 = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question1);

        Question question2 = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question2);

        TestQuestionID testQuestionID1 = new TestQuestionID(test, question1);

        TestQuestionID testQuestionID2 = new TestQuestionID(test, question2);

        ErrorReport errorReport1 = new ErrorReport(testQuestionID1, "1");
        errorReportsRepository.save(errorReport1);

        ErrorReport errorReport2 = new ErrorReport(testQuestionID2, "2");
        errorReportsRepository.save(errorReport2);

        MvcResult mvcResult = mockMvc.perform(get("/error_reports/{testId}", test.getId())
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        List<ErrorReportDTO> reports = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertEquals(2, reports.size());
        Assertions.assertTrue(reports.contains(new ErrorReportDTO(errorReport1)));
        Assertions.assertTrue(reports.contains(new ErrorReportDTO(errorReport2)));
    }

    @Test
    void getReportsFail() throws Exception {
        mockMvc.perform(get("/error_reports/{testId}", BAD_TEST_ID)
                .with(user(userDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void addSuccess() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user);
        testsRepository.save(test);

        Question question = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question);

        TestQuestionID testQuestionID = new TestQuestionID(test, question);

        ErrorReportDTO errorReportDTO = EntityCreatorUtil
                .createErrorReportDTO(GOOD_REPORT_BODY, question.getId(), test.getId());

        mockMvc.perform(post("/error_reports/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(errorReportDTO))
                .with(user(userDetails)))
                .andExpect(status().isOk());


        Optional<ErrorReport> report = errorReportsRepository.findById(testQuestionID);
        Assertions.assertTrue(report.isPresent());
        Assertions.assertEquals(GOOD_REPORT_BODY, report.get().getReportBody());
    }


    @Test
    void addFailReportNotFound() throws Exception{

        ErrorReportDTO errorReportDTO = EntityCreatorUtil
                .createErrorReportDTO(BAD_REPORT_BODY, BAD_QUESTION_ID, BAD_TEST_ID);

        mockMvc.perform(post("/error_reports/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(errorReportDTO))
                .with(user(userDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateReportBodySuccess() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user);
        testsRepository.save(test);

        Question question = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question);

        TestQuestionID testQuestionID = new TestQuestionID(test, question);

        ErrorReport errorReport = new ErrorReport(testQuestionID, GOOD_REPORT_BODY);
        errorReportsRepository.save(errorReport);

        ErrorReportDTO errorReportDTO = EntityCreatorUtil
                .createErrorReportDTO(NEW_REPORT_BODY, question.getId(), test.getId());

        mockMvc.perform(post("/error_reports/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(errorReportDTO))
                .with(user(userDetails)))
                .andExpect(status().isOk());

        Optional<ErrorReport> report = errorReportsRepository.findById(testQuestionID);
        Assertions.assertEquals(NEW_REPORT_BODY, report.get().getReportBody());
    }


    @Test
    void removeSuccess() throws Exception {
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user);
        testsRepository.save(test);

        Question question = EntityCreatorUtil.createQuestion(user);
        questionRepository.save(question);

        TestQuestionID testQuestionID = new TestQuestionID(test, question);

        ErrorReport errorReport = new ErrorReport(testQuestionID, GOOD_REPORT_BODY);
        errorReportsRepository.save(errorReport);


        mockMvc.perform(delete("/error_reports/")
                .param("testId", test.getId().toString())
                .param("questionId", question.getId().toString())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        Optional<ErrorReport> report = errorReportsRepository.findById(testQuestionID);
        Assertions.assertTrue(report.isEmpty());
    }

    @Test
    void removeFail() throws Exception{
        mockMvc.perform(delete("/error_reports/")
                .param("testId", String.valueOf(BAD_TEST_ID))
                .param("questionId", String.valueOf(BAD_QUESTION_ID))
                .with(user(userDetails)))
                .andExpect(status().isNotFound());
    }

}
