package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.FileAnswerRequest;
import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.FileAnswerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileAnswerControllerTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private FileAnswerRepository fileAnswerRepository;

    @Mock
    private FileAnswerService fileAnswerService;

    @Mock
    private FileAnswer fileAnswer;

    @InjectMocks
    private FileAnswerController fileAnswerController;
}
