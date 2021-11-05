package com.team4.testingsystem.model.dto;

import com.team4.testingsystem.model.entity.ChosenOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChosenOptionDTO implements Serializable {
    private Long testId;
    private Long questionId;
    private Long answerId;

    public ChosenOptionDTO(ChosenOption chosenOption) {
        this.testId = chosenOption.getId().getTest().getId();
        this.questionId = chosenOption.getId().getQuestion().getId();
        this.answerId = chosenOption.getAnswer().getId();
    }

}
