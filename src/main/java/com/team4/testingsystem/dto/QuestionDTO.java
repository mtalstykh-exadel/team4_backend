package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.Question;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class QuestionDTO implements Serializable {
    private Long id;
    private String questionBody;
    private String creator;
    private String level;
    private String module;
    private List<AnswerDTO> answers;

    private QuestionDTO(Question question, List<AnswerDTO> answers) {
        this.id = question.getId();
        this.questionBody = question.getBody();
        this.creator = question.getCreator().getName();
        this.level = question.getLevel().getName();
        this.module = question.getModule().getName();
        this.answers = answers;
    }

    public static QuestionDTO create(Question question) {
        if (question.getAnswers() == null) {
            return new QuestionDTO(question, null);
        }
        List<AnswerDTO> answers = question.getAnswers().stream()
                .map(AnswerDTO::create)
                .collect(Collectors.toList());

        return new QuestionDTO(question, answers);
    }

    public static QuestionDTO createWithCorrectAnswers(Question question) {
        if (question.getAnswers() == null) {
            return new QuestionDTO(question, null);
        }
        List<AnswerDTO> answers = question.getAnswers().stream()
                .map(AnswerDTO::createWithCorrect)
                .collect(Collectors.toList());

        return new QuestionDTO(question, answers);
    }

}
