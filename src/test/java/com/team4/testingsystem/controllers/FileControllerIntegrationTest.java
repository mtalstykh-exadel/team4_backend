package com.team4.testingsystem.controllers;

import com.team4.testingsystem.model.entity.User;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.impl.FileStorageForTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class FileControllerIntegrationTest {

    private final MockMvc mockMvc;

    private final FileStorageForTests fileStorage;

    private final UsersRepository usersRepository;

    private CustomUserDetails userDetails;

    private static final String FILE_PATH = "file.txt";

    private static final String FILE_CONTENT = "test data";

    private static final String WRONG_FILE_NAME = "non-existent file.txt";

    @Autowired
    FileControllerIntegrationTest(MockMvc mockMvc,
                                  FileStorageForTests fileStorage,
                                  UsersRepository usersRepository) {
        this.mockMvc = mockMvc;
        this.fileStorage = fileStorage;
        this.usersRepository = usersRepository;
    }

    @BeforeEach
    void init() {
        User user = usersRepository.findAll().iterator().next();
        userDetails = new CustomUserDetails(user);

        Resource resource = new ByteArrayResource(FILE_CONTENT.getBytes());
        fileStorage.save(FILE_PATH, resource);
    }

    @Test
    void downloadSuccess() throws Exception {
        mockMvc.perform(get("/files/{url}", FILE_PATH)
                .with(user(userDetails)))
                .andExpect(status().isOk());
    }

    @Test
    void downloadInternalServerError() throws Exception {
        mockMvc.perform(get("/files/{url}", WRONG_FILE_NAME)
                .with(user(userDetails)))
                .andExpect(status().isExpectationFailed());
    }

    @Test
    void downloadNotFound() throws Exception {
        mockMvc.perform(get("/files/{url}", WRONG_FILE_NAME)
                .with(user(userDetails)))
                .andExpect(status().isExpectationFailed());
    }

    @Test
    void uploadSuccess() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", FILE_PATH,
                MediaType.MULTIPART_FORM_DATA_VALUE, FILE_CONTENT.getBytes());

        mockMvc.perform(multipart("/files/listening")
                .file(mockMultipartFile)
                .with(user(userDetails)))
                .andExpect(status().isOk());
    }

    @AfterEach
    void destroy() {
        fileStorage.delete(FILE_PATH);
        Assertions.assertThrows(FileLoadingFailedException.class, () -> fileStorage.load(FILE_PATH));
    }
}
