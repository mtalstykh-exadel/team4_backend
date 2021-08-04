package com.team4.testingsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team4.testingsystem.entities.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestDTO {
    private String level;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private UserDTO coach;
    private List<QuestionDTO> grammarQuestions;
    private List<QuestionDTO> listeningQuestions;
    private QuestionDTO essayQuestion;
    private QuestionDTO speakingQuestion;
    private String contentFile;

    public TestDTO() {
    }

    public TestDTO(Test test) {
        level = test.getLevel().getName();
        createdAt = test.getCreatedAt();
        finishedAt = test.getFinishedAt();
        if (test.getCoach() != null) {
            coach = new UserDTO(test.getCoach());
        }
    }

    public List<QuestionDTO> getGrammarQuestions() {
        return grammarQuestions;
    }

    public void setGrammarQuestions(List<QuestionDTO> grammarQuestions) {
        this.grammarQuestions = grammarQuestions;
    }

    public List<QuestionDTO> getListeningQuestions() {
        return listeningQuestions;
    }

    public void setListeningQuestions(List<QuestionDTO> listeningQuestions) {
        this.listeningQuestions = listeningQuestions;
    }

    public QuestionDTO getEssayQuestion() {
        return essayQuestion;
    }

    public void setEssayQuestion(QuestionDTO essayQuestion) {
        this.essayQuestion = essayQuestion;
    }

    public QuestionDTO getSpeakingQuestion() {
        return speakingQuestion;
    }

    public void setSpeakingQuestion(QuestionDTO speakingQuestion) {
        this.speakingQuestion = speakingQuestion;
    }

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public UserDTO getCoach() {
        return coach;
    }

    public void setCoach(UserDTO coach) {
        this.coach = coach;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestDTO testDTO = (TestDTO) o;
        return Objects.equals(level, testDTO.level)
               && Objects.equals(createdAt, testDTO.createdAt)
               && Objects.equals(finishedAt, testDTO.finishedAt)
               && Objects.equals(coach, testDTO.coach)
               && Objects.equals(grammarQuestions, testDTO.grammarQuestions)
               && Objects.equals(listeningQuestions, testDTO.listeningQuestions)
               && Objects.equals(essayQuestion, testDTO.essayQuestion)
               && Objects.equals(speakingQuestion, testDTO.speakingQuestion)
               && Objects.equals(contentFile, testDTO.contentFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level,
                createdAt,
                finishedAt,
                coach,
                grammarQuestions,
                listeningQuestions,
                essayQuestion,
                speakingQuestion,
                contentFile);
    }
}
