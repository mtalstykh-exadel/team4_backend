package com.team4.testingsystem.entities;

import com.team4.testingsystem.dto.AnswerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name = "answer")
@NoArgsConstructor
@Getter
@Setter
public class Answer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "answer_body")
    private String answerBody;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "is_correct")
    private boolean isCorrect;

    public Answer(AnswerDTO answerDTO) {
        this.answerBody = answerDTO.getAnswer();
        this.isCorrect = answerDTO.getCorrect();
    }

    public Answer(AnswerDTO answerDTO, Question question) {
        this.answerBody = answerDTO.getAnswer();
        this.isCorrect = answerDTO.getCorrect();
        this.question = question;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Answer answer;

        public Builder() {
            this.answer = new Answer();
        }

        public Builder id(long id) {
            answer.id = id;
            return this;
        }

        public Builder answerBody(String answerBody) {
            answer.answerBody = answerBody;
            return this;
        }

        public Builder question(Question question) {
            answer.question = question;
            return this;
        }

        public Builder isCorrect(boolean isCorrect) {
            answer.isCorrect = isCorrect;
            return this;
        }

        public Answer build() {
            return answer;
        }
    }
}
