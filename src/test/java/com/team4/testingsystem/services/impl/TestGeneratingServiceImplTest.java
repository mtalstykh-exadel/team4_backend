package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.ContentFilesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestGeneratingServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ContentFilesService contentFilesService;

    @InjectMocks
    private TestGeneratingServiceImpl testGeneratingService;

    @Mock
    private List<Question> questions;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Mock
    private Level level;

    @Mock
    private ContentFile contentFile;

    @Test
    void formTestSuccess() {
        ReflectionTestUtils.setField(testGeneratingService, "count", 10);

        Mockito.when(questionRepository.
                getRandomQuestions(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(questions);
        Mockito.when(test.getLevel()).thenReturn(level);
        Mockito.when(level.getName()).thenReturn("Name");
        Mockito.when(contentFilesService.getRandomContentFile("Name")).thenReturn(contentFile);

        Mockito.when(questionRepository
                .getRandomQuestionByContentFile(anyLong(), any(Pageable.class)))
                .thenReturn(questions);

        testGeneratingService.formTest(test);
        verify(questions, times(4)).forEach(any());
        Assertions.assertEquals(test, testGeneratingService.formTest(test));
        }
}
