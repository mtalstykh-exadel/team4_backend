package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.exceptions.FileDeletingFailedException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import com.team4.testingsystem.services.FilesService;
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
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
class ResourceStorageServiceImplTest {
    @Mock
    private FilesService filesService;

    @InjectMocks
    private ResourceStorageServiceImpl fileStorage;

    private static final String SOURCE_FILE_NAME = "source_file.txt";

    private static final Path SOURCE_FILE_PATH = Path.of(SOURCE_FILE_NAME);

    private FileSystemResource sourceFile;

    @BeforeEach
    void init() {
        try {
            Files.createFile(SOURCE_FILE_PATH);
        } catch (IOException e) {
            Assertions.fail();
        }

        sourceFile = new FileSystemResource(SOURCE_FILE_NAME);
    }

    @AfterEach
    void destroy() {
        try {
            Files.delete(SOURCE_FILE_PATH);
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @Test
    void uploadFailed() {
        Mockito.doThrow(new FileSavingFailedException()).when(filesService)
                .save(Mockito.contains(SOURCE_FILE_NAME), Mockito.eq(sourceFile));

        Assertions.assertThrows(FileSavingFailedException.class,
                () -> fileStorage.upload(sourceFile));
    }

    @Test
    void uploadSuccess() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.doNothing().when(filesService)
                .save(captor.capture(), Mockito.eq(sourceFile));

        String fileUrl = fileStorage.upload(sourceFile);
        Assertions.assertEquals(fileUrl, captor.getValue());
    }

    @Test
    void loadFailed() {
        Mockito.when(filesService.load(SOURCE_FILE_NAME))
                .thenThrow(new FileLoadingFailedException());

        Assertions.assertThrows(FileLoadingFailedException.class,
                () -> fileStorage.load(SOURCE_FILE_NAME));
    }

    @Test
    void loadSuccess() {
        Mockito.when(filesService.load(SOURCE_FILE_NAME)).thenReturn(sourceFile);
        Assertions.assertEquals(sourceFile, filesService.load(SOURCE_FILE_NAME));
    }

    @Test
    void deleteFailed() {
        Mockito.doThrow(new FileDeletingFailedException())
                .when(filesService).delete(SOURCE_FILE_NAME);

        Assertions.assertThrows(FileDeletingFailedException.class,
                () -> fileStorage.delete(SOURCE_FILE_NAME));
    }

    @Test
    void deleteSuccess() {
        Mockito.doNothing().when(filesService).delete(SOURCE_FILE_NAME);
        Assertions.assertDoesNotThrow(() -> fileStorage.delete(SOURCE_FILE_NAME));
    }
}
