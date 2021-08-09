package com.team4.testingsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team4.testingsystem.entities.Answer;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerDTO {
    private Long id;
    private String answer;
    private Boolean correct;

    public AnswerDTO() {
    }

    public AnswerDTO(Answer answer) {
        this.id = answer.getId();
        this.answer = answer.getAnswerBody();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnswerDTO answerDTO = (AnswerDTO) o;
        return Objects.equals(answer, answerDTO.answer)
               && Objects.equals(correct, answerDTO.correct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer, correct);
    }
}
