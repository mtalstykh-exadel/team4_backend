package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.services.FileAnswerService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FileAnswerControllerTest {

    @Mock
    private MultipartFile file;

    @Mock
    private FileAnswerService fileAnswerService;

    @Mock
    private FileAnswer fileAnswer;

    @InjectMocks
    private FileAnswerController fileAnswerController;

    @Test
    void uploadSpeaking() {
        Mockito.when(fileAnswerService.addFileAnswer(any(), any(), any())).thenReturn(fileAnswer);
        Mockito.when(fileAnswer.getUrl()).thenReturn("some url");
        Assertions.assertEquals("some url", fileAnswerController.uploadSpeaking(file, EntityCreatorUtil.ID));
    }

    @Test
    void downloadSpeaking() {
        Mockito.when(fileAnswerService.getSpeaking(any())).thenReturn("some url");
        Assertions.assertEquals("some url", fileAnswerController.downloadSpeaking(EntityCreatorUtil.ID));
    }
}
