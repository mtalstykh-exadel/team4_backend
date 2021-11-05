package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.model.entity.ContentFile;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.exceptions.ContentFileNotFoundException;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.RestrictionsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContentFilesServiceImplTest {

    @Mock
    private ContentFile contentFile;

    @Mock
    private ContentFilesRepository contentFilesRepository;

    @Mock
    private RestrictionsService restrictionsService;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private ContentFilesServiceImpl contentFilesService;

    private static final String URL = "https://best_listening_audios.com/";
    private static final String OLD_URL = "https://best_listening_audios.com/old";
    private static final boolean UNAVAILABLE = false;
    private static final Long ID = 1L;
    private static final Long BAD_ID = 42L;
    private static final Levels A1 = Levels.A1;

    @Test
    void updateWithFile() {
        ContentFile oldContentFile = Mockito.mock(ContentFile.class);
        Mockito.when(contentFilesRepository.findById(ID)).thenReturn(Optional.of(oldContentFile));

        Mockito.when(oldContentFile.getUrl()).thenReturn(OLD_URL);
        Mockito.when(contentFile.getUrl()).thenReturn(URL);

        Mockito.when(contentFilesRepository.save(contentFile)).thenReturn(contentFile);
        ContentFile result = contentFilesService.update(ID, contentFile);

        Mockito.verify(restrictionsService).checkNotArchivedContentFile(contentFile);
        Mockito.verify(contentFilesRepository).updateAvailable(ID, false);
        Assertions.assertEquals(contentFile, result);
    }

    @Test
    void updateWithUrl() {
        ContentFile oldContentFile = Mockito.mock(ContentFile.class);
        Mockito.when(contentFilesRepository.findById(ID)).thenReturn(Optional.of(oldContentFile));
        Mockito.when(oldContentFile.getUrl()).thenReturn(OLD_URL);
        Mockito.when(contentFile.getUrl()).thenReturn(URL);
        Mockito.when(contentFilesRepository.save(contentFile)).thenReturn(contentFile);
        contentFilesService.update(ID, contentFile);
        verify(restrictionsService).checkNotArchivedContentFile(contentFile);
        verify(restrictionsService, Mockito.times(2)).checkFileExists(URL);

        Mockito.verify(contentFile).setId(null);
        Assertions.assertEquals(contentFile, contentFilesService.update(ID, contentFile));
    }

    @Test
    void updateWithoutUrl() {
        ContentFile oldContentFile = Mockito.mock(ContentFile.class);
        Mockito.when(contentFilesRepository.findById(ID)).thenReturn(Optional.of(oldContentFile));
        Mockito.when(oldContentFile.getUrl()).thenReturn(OLD_URL);
        Mockito.when(contentFile.getUrl()).thenReturn(null,null, OLD_URL);
        Mockito.when(contentFilesRepository.save(contentFile)).thenReturn(contentFile);
        ContentFile result = contentFilesService.update(ID, contentFile);

        Mockito.verify(contentFile).setUrl(oldContentFile.getUrl());
        Mockito.verify(contentFile).setId(ID);
        Mockito.verify(questionService).archiveQuestionsByContentFileId(ID);
        Assertions.assertEquals(OLD_URL, result.getUrl());
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

        Mockito.when(contentFile.getUrl()).thenReturn(URL);
        contentFilesService.add(contentFile);

        verify(restrictionsService).checkListeningHasAudio(contentFile);
        verify(restrictionsService).checkFileExists(URL);
        verify(contentFilesRepository).save(contentFile);
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

        Mockito.verify(contentFilesRepository).updateAvailable(ID, UNAVAILABLE);
        Mockito.verify(questionService).archiveQuestionsByContentFileId(ID);
        Assertions.assertDoesNotThrow(() -> contentFilesService.updateAvailability(ID, UNAVAILABLE));
    }

    @Test
    void archiveFail() {
        Mockito.when(contentFilesRepository.updateAvailable(BAD_ID, UNAVAILABLE)).thenReturn(0);

        Assertions.assertThrows(ContentFileNotFoundException.class,
                () -> contentFilesService.updateAvailability(BAD_ID, UNAVAILABLE));
    }

    @Test
    void getRandomContentFileSuccess() {
        ContentFile contentFile = new ContentFile();
        Mockito.when(contentFilesRepository.getRandomFile(A1.name())).thenReturn(Optional.of(contentFile));

        Assertions.assertEquals(contentFile, contentFilesService.getRandomContentFile(A1.name()));
    }

    @Test
    void getRandomContentFileFail() {
        Mockito.when(contentFilesRepository.getRandomFile(A1.name())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> contentFilesService.getRandomContentFile(A1.name()));
    }

    @Test
    void getContentFileByQuestionId() {
        ContentFile contentFile = new ContentFile();
        Mockito.when(contentFilesRepository.getContentFileByQuestionId(ID)).thenReturn(contentFile);

        Assertions.assertEquals(contentFile, contentFilesService.getContentFileByQuestionId(ID));
    }
}
