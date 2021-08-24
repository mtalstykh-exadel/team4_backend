package com.team4.testingsystem.entities;

import com.team4.testingsystem.dto.AnswerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@Data
@Builder
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

}
