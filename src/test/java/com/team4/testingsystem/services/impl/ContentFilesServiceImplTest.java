package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContentFilesServiceImplTest {

    @Mock
    ContentFile contentFile;

    @Mock
    List<Question> questions;

    @Mock
    ContentFilesRepository contentFilesRepository;

    @Mock
    QuestionService questionService;

    @InjectMocks
    ContentFilesServiceImpl contentFilesService;

    @Test
    void update() {
        Mockito.when(contentFilesRepository.save(any())).thenReturn(contentFile);
        ContentFile result = contentFilesService.update(EntityCreatorUtil.ID, "some url", questions);
        verify(questionService).archiveQuestionsByContentFileId(EntityCreatorUtil.ID);
        verify(contentFilesRepository).archiveContentFile(EntityCreatorUtil.ID);
        Assertions.assertEquals(contentFile, result);
    }

    @Test
    void  updateQuestions() {
        Mockito.when(contentFilesRepository.findById(EntityCreatorUtil.ID))
                .thenReturn(Optional.ofNullable(contentFile));
        Mockito.when(contentFilesRepository.save(contentFile)).thenReturn(contentFile);
        Mockito.when(contentFile.getQuestions()).thenReturn(questions);
        ContentFile result = contentFilesService.updateQuestions(EntityCreatorUtil.ID, questions);
        verify(questionService).archiveQuestionsByContentFileId(EntityCreatorUtil.ID);
        Assertions.assertEquals(contentFile, result);
    }

    @Test
    void getAllSuccess() {
        List<ContentFile> contentFiles = new ArrayList<>();
        Mockito.when(contentFilesRepository.findAll()).thenReturn(contentFiles);

        Assertions.assertEquals(contentFiles, contentFilesService.getAll());
    }

    @Test
    void getByIdSuccess() {
        Mockito.when(contentFilesRepository.findById(1L)).thenReturn(Optional.of(contentFile));

        Assertions.assertEquals(contentFile, contentFilesService.getById(1L));
    }

    @Test
    void getByIdFail() {

        Mockito.when(contentFilesRepository.findById(42L)).thenThrow(FileNotFoundException.class);

        Assertions.assertThrows(FileNotFoundException.class, () -> contentFilesService.getById(42L));
    }

    @Test
    void addSuccess(){
        List<Question> questions = new ArrayList<>();
        contentFilesService.add("https://best_listening_audios.com/", questions);
        verify(contentFilesRepository).save(any());
    }

    @Test
    void updateUrlSuccess(){
        Mockito.when(contentFilesRepository.changeUrl("https://best_listening_audios.com/", 1L)).thenReturn(1);

        contentFilesService.updateURL(1L, "https://best_listening_audios.com/");

        verify(contentFilesRepository).changeUrl("https://best_listening_audios.com/", 1L);

        Assertions.assertDoesNotThrow(()->contentFilesService.updateURL(1L, "https://best_listening_audios.com/"));
    }

    @Test
    void updateUrlFail(){
        Mockito.when(contentFilesRepository.changeUrl("https://42.com/", 42L)).thenReturn(0);

        Assertions.assertThrows(FileNotFoundException.class,
                () -> contentFilesService.updateURL(42L, "https://42.com/"));
    }

    @Test
    void removeSuccess() {

        Mockito.when(contentFilesRepository.removeById(1L)).thenReturn(1);

        contentFilesService.removeById(1L);

        verify(contentFilesRepository).removeById(1L);

        Assertions.assertDoesNotThrow(()->contentFilesService.removeById(1L));

    }

    @Test
    void removeFail() {

        Mockito.when(contentFilesRepository.removeById(42L)).thenReturn(0);

        Assertions.assertThrows(FileNotFoundException.class, () -> contentFilesService.removeById(42L));

    }

    @Test
    void getRandomContentFiles() {
        ContentFile contentFile = new ContentFile();
        Mockito.when(contentFilesRepository.getRandomFiles(any())).thenReturn(contentFile);
        Assertions.assertEquals(contentFile, contentFilesService.getRandomContentFile(any()));
    }

    @Test
    void getContentFileByQuestionId() {
        ContentFile contentFile = new ContentFile();
        Mockito.when(contentFilesRepository.getContentFileByQuestionId(any())).thenReturn(contentFile);
        Assertions.assertEquals(contentFile, contentFilesService.getContentFileByQuestionId(any()));
    }

}
