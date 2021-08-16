package com.team4.testingsystem.controllers;

import com.team4.testingsystem.services.FilesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    @Mock
    private Resource resource;

    @Mock
    private FilesService filesService;

    private static final String URL = "some url";

    @InjectMocks
    private FileController fileController;

    @Test
    void download() {
        Mockito.when(filesService.load(URL)).thenReturn(resource);
        Assertions.assertEquals(resource, fileController.download(URL));
    }
}
