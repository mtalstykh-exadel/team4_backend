package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.exceptions.ContentFileNotFoundException;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.ResourceStorageService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import liquibase.pro.packaged.I;
import liquibase.pro.packaged.U;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContentFilesServiceImplTest {

    @Mock
    private MultipartFile file;

    @Mock
    private ContentFile contentFile;

    @Mock
    private ContentFilesRepository contentFilesRepository;

    @Mock
    private ResourceStorageService storageService;

    @Mock
    private QuestionService questionService;

    @Mock
    private CustomUserDetails userDetails;

    @InjectMocks
    private ContentFilesServiceImpl contentFilesService;

    private static final String URL = "https://best_listening_audios.com/";
    private static final Long USER_ID = 1L;
    private static final boolean UNAVAILABLE = false;
    private static final Long ID = 1L;
    private static final Long BAD_ID = 42L;
    private static final Levels A1 = Levels.A1;

    @Test
    void updateWithFile() {
        Mockito.when(userDetails.getId()).thenReturn(USER_ID);
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(contentFilesRepository.save(any())).thenReturn(contentFile);
            Mockito.when(contentFilesRepository.updateAvailable(ID, UNAVAILABLE)).thenReturn(1);
            ContentFile result = contentFilesService.update(file, ID, contentFile);

            verify(questionService).archiveQuestionsByContentFileId(ID);
            Assertions.assertEquals(contentFile, result);
        }
    }

    @Test
    void getAllSuccess() {
        List<ContentFile> contentFiles = new ArrayList<>();
        Mockito.when(contentFilesRepository.findAll()).thenReturn(contentFiles);

        Assertions.assertEquals(contentFiles, contentFilesService.getAll());
    }

    @Test
    void getByIdSuccess() {
        Mockito.when(contentFilesRepository.findById(ID)).thenReturn(Optional.of(contentFile));

        Assertions.assertEquals(contentFile, contentFilesService.getById(ID));
    }

    @Test
    void getByIdFail() {
        Mockito.when(contentFilesRepository.findById(BAD_ID)).thenThrow(FileNotFoundException.class);

        Assertions.assertThrows(FileNotFoundException.class, () -> contentFilesService.getById(BAD_ID));
    }

    @Test
    void addSuccess() {
        Mockito.when(userDetails.getId()).thenReturn(USER_ID);
        Mockito.when(storageService.upload(file.getResource(), Modules.LISTENING, USER_ID)).thenReturn(URL);
        try (final MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            contentFilesService.add(file, contentFile);

            verify(contentFilesRepository).save(any());
        }
    }

    @Test
    void updateUrlSuccess() {
        Mockito.when(contentFilesRepository.changeUrl(URL, ID)).thenReturn(1);
        contentFilesService.updateURL(ID, URL);

        verify(contentFilesRepository).changeUrl(URL, ID);
        Assertions.assertDoesNotThrow(() -> contentFilesService.updateURL(ID, URL));
    }

    @Test
    void updateUrlFail() {
        Mockito.when(contentFilesRepository.changeUrl(URL, BAD_ID)).thenReturn(0);

        Assertions.assertThrows(ContentFileNotFoundException.class,
                () -> contentFilesService.updateURL(BAD_ID, URL));
    }

    @Test
    void archiveSuccess() {
        Mockito.when(contentFilesRepository.updateAvailable(ID, UNAVAILABLE)).thenReturn(1);
        contentFilesService.updateAvailability(ID, UNAVAILABLE);

        verify(contentFilesRepository).updateAvailable(ID, UNAVAILABLE);
        Assertions.assertDoesNotThrow(() -> contentFilesService.updateAvailability(ID, UNAVAILABLE));
    }

    @Test
    void archiveFail() {
        Mockito.when(contentFilesRepository.updateAvailable(BAD_ID, UNAVAILABLE)).thenReturn(0);

        Assertions.assertThrows(ContentFileNotFoundException.class,
            () -> contentFilesService.updateAvailability(BAD_ID, UNAVAILABLE));
    }

    @Test
    void getRandomContentFiles() {
        ContentFile contentFile = new ContentFile();
        Mockito.when(contentFilesRepository.getRandomFiles(A1.name())).thenReturn(contentFile);

        Assertions.assertEquals(contentFile, contentFilesService.getRandomContentFile(A1.name()));
    }

    @Test
    void getContentFileByQuestionId() {
        ContentFile contentFile = new ContentFile();
        Mockito.when(contentFilesRepository.getContentFileByQuestionId(ID)).thenReturn(contentFile);

        Assertions.assertEquals(contentFile, contentFilesService.getContentFileByQuestionId(ID));
    }
}
