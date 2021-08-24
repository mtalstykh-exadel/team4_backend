package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.Answer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class AnswerDTO implements Serializable {
    private Long id;
    private String answer;
    private Boolean correct;
    private Boolean checked;

    private AnswerDTO(Answer answer) {
        this.id = answer.getId();
        this.answer = answer.getAnswerBody();
    }

    public static AnswerDTO create(Answer answer) {
        return new AnswerDTO(answer);
    }

    public static AnswerDTO createWithCorrect(Answer answer) {
        AnswerDTO answerDTO = new AnswerDTO(answer);
        answerDTO.setCorrect(answer.isCorrect());
        return answerDTO;
    }

}
