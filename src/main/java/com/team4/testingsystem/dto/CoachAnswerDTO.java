package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.CoachAnswer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoachAnswerDTO implements Serializable {
    private Long questionId;
    private Long testId;
    private String comment;

    public CoachAnswerDTO(CoachAnswer coachAnswer) {
        questionId = coachAnswer.getId().getQuestion().getId();
        testId = coachAnswer.getId().getTest().getId();
        comment = coachAnswer.getComment();
    }

}
