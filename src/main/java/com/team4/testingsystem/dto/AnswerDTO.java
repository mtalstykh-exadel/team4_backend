package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.Answer;

import java.io.Serializable;
import java.util.Objects;

public class AnswerDTO implements Serializable {
    private Long id;
    private String answer;
    private Boolean correct;

    public AnswerDTO() {
    }

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
