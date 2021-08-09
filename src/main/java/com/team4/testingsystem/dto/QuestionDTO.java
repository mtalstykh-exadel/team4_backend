package com.team4.testingsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team4.testingsystem.entities.Question;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionDTO {
    private String questionBody;
    private String creator;
    private String level;
    private String module;
    private List<AnswerDTO> answers;

    public QuestionDTO() {
    }

    private QuestionDTO(Question question, List<AnswerDTO> answers) {
        this.questionBody = question.getBody();
        this.creator = question.getCreator().getName();
        this.level = question.getLevel().getName();
        this.module = question.getModule().getName();
        this.answers = answers;
    }

    public static QuestionDTO create(Question question) {
        List<AnswerDTO> answers = question.getAnswers().stream()
                .map(AnswerDTO::create)
                .collect(Collectors.toList());

        return new QuestionDTO(question, answers);
    }

    public static QuestionDTO createWithCorrectAnswers(Question question) {
        List<AnswerDTO> answers = question.getAnswers().stream()
                .map(AnswerDTO::createWithCorrect)
                .collect(Collectors.toList());

        return new QuestionDTO(question, answers);
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QuestionDTO that = (QuestionDTO) o;
        return Objects.equals(questionBody, that.questionBody)
               && Objects.equals(creator, that.creator)
               && Objects.equals(level, that.level)
               && Objects.equals(module, that.module)
               && Objects.equals(answers, that.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionBody, creator, level, module, answers);
    }
}
