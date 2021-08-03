package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
class FileSystemServiceTest {
    @Spy
    private FileSystemService fileSystemService;

    private static final String SOURCE_FILE_NAME = "source_file.txt";
    private static final String FILE_NAME = "test_file.txt";
    private static final String TEST_CONTENT = "test content";

    private static final Path SOURCE_FILE_PATH = Path.of(SOURCE_FILE_NAME);
    private static final Path FILE_PATH = Path.of(FILE_NAME);

    private FileSystemResource sourceFile;

    @BeforeEach
    void init() {
        try {
            Files.writeString(SOURCE_FILE_PATH, TEST_CONTENT);
        } catch (IOException e) {
            Assertions.fail();
        }

        sourceFile = new FileSystemResource(SOURCE_FILE_NAME);
    }

    @AfterEach
    void destroy() {
        try {
            Files.deleteIfExists(FILE_PATH);
            Files.delete(SOURCE_FILE_PATH);
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @Test
    void saveFileSuccess() {
        Mockito.when(fileSystemService.generateFilePath(FILE_NAME)).thenReturn(FILE_PATH);

        fileSystemService.save(FILE_NAME, sourceFile);

        Assertions.assertTrue(Files.exists(FILE_PATH));
        try {
            Assertions.assertEquals(TEST_CONTENT, Files.readString(FILE_PATH));
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @Test
    void saveFileAlreadyExists() {
        Mockito.when(fileSystemService.generateFilePath(FILE_NAME)).thenReturn(FILE_PATH);
        try {
            Files.writeString(FILE_PATH, TEST_CONTENT + TEST_CONTENT);
        } catch (IOException e) {
            Assertions.fail();
        }

        Assertions.assertDoesNotThrow(() -> fileSystemService.save(FILE_NAME, sourceFile));

        Assertions.assertTrue(Files.exists(FILE_PATH));
        try {
            Assertions.assertEquals(TEST_CONTENT, Files.readString(FILE_PATH));
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @Test
    void saveFileDirectoryNotExists() {
        Path nonExistingPath = Path.of("non-existing-dir/" + FILE_NAME);
        Mockito.when(fileSystemService.generateFilePath(FILE_NAME)).thenReturn(nonExistingPath);

        Assertions.assertDoesNotThrow(() -> fileSystemService.save(FILE_NAME, sourceFile));

        Assertions.assertTrue(Files.exists(nonExistingPath));

        try {
            Files.delete(nonExistingPath);
            Files.delete(nonExistingPath.getParent());
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @Test
    void loadFileNotExists() {
        Mockito.when(fileSystemService.generateFilePath(FILE_NAME)).thenReturn(FILE_PATH);

        Assertions.assertThrows(FileLoadingFailedException.class,
                () -> fileSystemService.load(FILE_NAME));
    }

    @Test
    void loadFileSuccess() {
        Mockito.when(fileSystemService.generateFilePath(FILE_NAME)).thenReturn(FILE_PATH);
        try {
            Files.writeString(FILE_PATH, TEST_CONTENT);
        } catch (IOException e) {
            Assertions.fail();
        }

        try {
            Assertions.assertArrayEquals(TEST_CONTENT.getBytes(StandardCharsets.UTF_8),
                    fileSystemService.load(FILE_NAME).getInputStream().readAllBytes());
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @Test
    void deleteFileNotExists() {
        Mockito.when(fileSystemService.generateFilePath(FILE_NAME)).thenReturn(FILE_PATH);

        Assertions.assertDoesNotThrow(() -> fileSystemService.delete(FILE_NAME));
    }

    @Test
    void deleteFileSuccess() {
        Mockito.when(fileSystemService.generateFilePath(FILE_NAME)).thenReturn(FILE_PATH);
        try {
            Files.writeString(FILE_PATH, TEST_CONTENT);
        } catch (IOException e) {
            Assertions.fail();
        }

        Assertions.assertDoesNotThrow(() -> fileSystemService.delete(FILE_NAME));
        Assertions.assertTrue(Files.notExists(FILE_PATH));
    }

    @Test
    void saveLoadDelete() {
        Assertions.assertDoesNotThrow(() -> fileSystemService.save(FILE_NAME, sourceFile));

        try {
            Assertions.assertArrayEquals(TEST_CONTENT.getBytes(StandardCharsets.UTF_8),
                    fileSystemService.load(FILE_NAME).getInputStream().readAllBytes());
        } catch (IOException e) {
            Assertions.fail();
        }

        Assertions.assertDoesNotThrow(() -> fileSystemService.delete(FILE_NAME));
    }
}
