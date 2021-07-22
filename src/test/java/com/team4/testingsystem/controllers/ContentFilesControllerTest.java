package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.services.ContentFilesService;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContentFilesControllerTest {

    @Mock
    ContentFile contentFile;

    @Mock
    ContentFilesService contentFilesService;

    @InjectMocks
    ContentFilesController contentFilesController;

    @Test
    void getAllSuccess() {
        List<ContentFile> tests = new ArrayList<>();

        Mockito.when(contentFilesService.getAll()).thenReturn(tests);

        Assertions.assertEquals(tests, contentFilesController.getAll());
    }

    @Test
    void getByIdSuccess() {
        Mockito.when(contentFilesService.getById(1L)).thenReturn(contentFile);

        Assertions.assertEquals(contentFile, contentFilesController.getById(1L));
    }

    @Test
    void getByIdFail() {
        Mockito.when(contentFilesService.getById(42L)).thenThrow(FileNotFoundException.class);

        Assertions.assertThrows(FileNotFoundException.class, () -> contentFilesController.getById(42L));
    }

    @Test
    void addSuccess() {
        contentFilesController.add(EntityCreatorUtil.createContentFileRequest(1L, "https://best_listening_audios.com/"));

        verify(contentFilesService).add("https://best_listening_audios.com/", 1L);
    }

    @Test
    void addFail() {

        doThrow(QuestionNotFoundException.class).when(contentFilesService).add("https://42.com/", 42L);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> contentFilesController.add(EntityCreatorUtil
                        .createContentFileRequest(42L, "https://42.com/")));
    }


    @Test
    void updateUrlSuccess() {

        contentFilesController.updateUrl(1L, "https://best_listening_audios.com/");

        verify(contentFilesService).updateURL(1L, "https://best_listening_audios.com/");
    }

    @Test
    void updateUrlFail() {

        doThrow(FileNotFoundException.class).when(contentFilesService).updateURL(42L, "https://42.com/");

        Assertions.assertThrows(FileNotFoundException.class,
                ()-> contentFilesController.updateUrl(42L, "https://42.com/"));
    }

    @Test
    void removeByIdSuccess() {

        contentFilesController.removeById(1L);

        verify(contentFilesService).removeById(1L);
    }

    @Test
    void removeByIdFail() {

        doThrow(FileNotFoundException.class).when(contentFilesService).removeById(42L);

        Assertions.assertThrows(FileNotFoundException.class, ()-> contentFilesController.removeById(42L));
    }

}
