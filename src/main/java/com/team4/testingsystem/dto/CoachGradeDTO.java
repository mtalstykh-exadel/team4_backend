package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.CoachGrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CoachGradeDTO implements Serializable {
    private Long testId;
    private Long questionId;
    private Integer grade;
    private String comment;

    public CoachGradeDTO(CoachGrade coachGrade) {
        this.testId = coachGrade.getId().getTest().getId();
        this.questionId = coachGrade.getId().getQuestion().getId();
        this.grade = coachGrade.getGrade();
        this.comment = coachGrade.getComment();
    }

}
