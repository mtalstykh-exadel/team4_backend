package com.team4.testingsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TestVerificationDTO implements Serializable {
    private Long testId;
    private String testLevel;
    private List<ReportedQuestionDTO> reportedQuestions;
    private QuestionDTO essayQuestion;
    private String essayText;
    private QuestionDTO speakingQuestion;
    private String speakingUrl;
    private List<CoachGradeDTO> grades;
    private List<CoachAnswerDTO> coachAnswers;

}
