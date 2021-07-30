package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerIntegrationTest {

    private MockMvc mockMvc;

    private UsersRepository usersRepository;

    private CustomUserDetails userDetails;

    @Autowired
    FileControllerIntegrationTest(MockMvc mockMvc, UsersRepository usersRepository) {
        this.mockMvc = mockMvc;
        this.usersRepository = usersRepository;
    }

    @BeforeEach
    void init() {
        User user = usersRepository.findAll().iterator().next();
        userDetails = new CustomUserDetails(user);
    }

    @Test
    void downloadSuccess() throws Exception {
        mockMvc.perform(get("/files/{url}", "5703afe3-c926-4c02-bb04-7a5a45620336-file.txt")
                .with(user(userDetails)))
                .andExpect(status().isOk());
    }

    @Test
    void uploadSuccess() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "file.txt",
                MediaType.MULTIPART_FORM_DATA_VALUE, "test data".getBytes());

        MockHttpServletRequestBuilder builder = multipart("/files/").file(mockMultipartFile)
                .with(user(userDetails));

        mockMvc.perform(builder).andExpect(status().isOk());

    }
}