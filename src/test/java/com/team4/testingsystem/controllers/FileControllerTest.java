package com.team4.testingsystem.controllers;

import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.ResourceStorageService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    @Mock
    private Resource resource;

    @Mock
    private ResourceStorageService resourceStorageService;

    private static final String URL = "some url";
    private static final Long USER_ID = 1L;

    @InjectMocks
    private FileController fileController;

    @Mock
    private CustomUserDetails userDetails;

    @Mock
    private MultipartFile file;

    @Mock
    private Resource fileResource;

    @Test
    void download() {
        Mockito.when(resourceStorageService.load(URL)).thenReturn(resource);
        Assertions.assertEquals(resource, fileController.download(URL));
    }

    @Test
    void uploadListeningSavingFailed() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(USER_ID);

            Mockito.when(file.getResource()).thenReturn(fileResource);
            Mockito.when(resourceStorageService.upload(fileResource, Modules.LISTENING, USER_ID))
                    .thenThrow(FileSavingFailedException.class);

            Assertions.assertThrows(FileSavingFailedException.class,
                    () -> fileController.uploadListening(file));
        }
    }

    @Test
    void uploadListeningSuccess() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(USER_ID);

            Mockito.when(file.getResource()).thenReturn(fileResource);
            Mockito.when(resourceStorageService.upload(fileResource, Modules.LISTENING, USER_ID))
                    .thenReturn(URL);

            Assertions.assertEquals(URL, fileController.uploadListening(file));
        }
    }
}
