package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.CoachAnswerDTO;
import com.team4.testingsystem.services.CoachAnswerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CoachAnswerControllerTest {
    @Mock
    private CoachAnswerService coachAnswerService;

    @Mock
    private List<CoachAnswerDTO> coachAnswers;

    @InjectMocks
    private CoachAnswerController coachAnswerController;

    @Test
    void addAll() {

        coachAnswerController.addAll(coachAnswers);

        Mockito.verify(coachAnswerService).addAll(coachAnswers);

    }
}