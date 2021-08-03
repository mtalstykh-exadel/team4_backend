package com.team4.testingsystem.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.team4.testingsystem.exceptions.FileDeletingFailedException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
class AmazonS3ServiceTest {
    @Mock
    private AmazonS3 amazonS3;

    @InjectMocks
    private AmazonS3Service amazonS3Service;

    @Value("${cloud.s3.bucket-name}")
    private String bucketName;

    private static final String SOURCE_FILE_NAME = "source_file.txt";
    private static final String FILE_NAME = "test_file.txt";
    private static final String TEST_CONTENT = "test content";

    private static final Path SOURCE_FILE_PATH = Path.of(SOURCE_FILE_NAME);
    private static final Path FILE_PATH = Path.of(FILE_NAME);

    private FileSystemResource sourceFile;

    @BeforeEach
    void init() {
        amazonS3Service = new AmazonS3Service(amazonS3);

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
    void saveFail() {
        ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
        Mockito.when(amazonS3.putObject(
                Mockito.eq(bucketName),
                Mockito.eq(FILE_NAME),
                fileArgumentCaptor.capture())
        ).thenThrow(new AmazonServiceException(""));

        Assertions.assertThrows(FileSavingFailedException.class,
                () -> amazonS3Service.save(FILE_NAME, sourceFile));
    }

    @Test
    void saveSuccess() throws IOException {
        AmazonS3Service spyRepository = Mockito.spy(amazonS3Service);
        Mockito.when(spyRepository.createTempFile(FILE_NAME)).thenReturn(FILE_PATH);

        spyRepository.save(FILE_NAME, sourceFile);

        ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
        Mockito.verify(amazonS3).putObject(
                Mockito.eq(bucketName),
                Mockito.eq(FILE_NAME),
                fileArgumentCaptor.capture()
        );

        Assertions.assertEquals(FILE_PATH.toString(), fileArgumentCaptor.getValue().getPath());
        try {
            Assertions.assertEquals(TEST_CONTENT, Files.readString(FILE_PATH));
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @Test
    void loadFail() {
        Mockito.when(amazonS3.getObject(bucketName, FILE_NAME))
                .thenThrow(new AmazonServiceException(""));

        Assertions.assertThrows(FileLoadingFailedException.class,
                () -> amazonS3Service.load(FILE_NAME));
    }

    @Test
    void loadSuccess() throws IOException {
        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(sourceFile.getInputStream());

        Mockito.when(amazonS3.getObject(bucketName, FILE_NAME)).thenReturn(s3Object);

        Assertions.assertArrayEquals(TEST_CONTENT.getBytes(StandardCharsets.UTF_8),
                amazonS3Service.load(FILE_NAME).getInputStream().readAllBytes());
    }

    @Test
    void deleteFail() {
        Mockito.doThrow(new AmazonServiceException(""))
                .when(amazonS3).deleteObject(bucketName, FILE_NAME);

        Assertions.assertThrows(FileDeletingFailedException.class,
                () -> amazonS3Service.delete(FILE_NAME));
    }

    @Test
    void deleteSuccess() {
        Mockito.doNothing().when(amazonS3).deleteObject(bucketName, FILE_NAME);

        Assertions.assertDoesNotThrow(() -> amazonS3Service.delete(FILE_NAME));
    }
}
