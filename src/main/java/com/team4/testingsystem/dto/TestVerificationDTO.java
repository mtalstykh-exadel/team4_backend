package com.team4.testingsystem.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class TestVerificationDTO implements Serializable {
    private Long testId;
    private String testLevel;
    private List<ReportedQuestionDTO> reportedQuestions;
    private QuestionDTO essayQuestion;
    private String essayText;
    private QuestionDTO speakingQuestion;
    private String speakingUrl;
    private List<CoachGradeDTO> grades;
    private List<CoachAnswerDTO> coachAnswers;

    public TestVerificationDTO() {
    }

    public Long getTestId() {
        return testId;
    }

    public List<CoachGradeDTO> getGradesDTO() {
        return grades;
    }

    public void setGradesDTO(List<CoachGradeDTO> gradesDTO) {
        this.grades = gradesDTO;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getTestLevel() {
        return testLevel;
    }

    public void setTestLevel(String testLevel) {
        this.testLevel = testLevel;
    }

    public List<ReportedQuestionDTO> getReportedQuestions() {
        return reportedQuestions;
    }

    public void setReportedQuestions(List<ReportedQuestionDTO> reportedQuestions) {
        this.reportedQuestions = reportedQuestions;
    }

    public QuestionDTO getEssayQuestion() {
        return essayQuestion;
    }

    public void setEssayQuestion(QuestionDTO essayQuestion) {
        this.essayQuestion = essayQuestion;
    }

    public String getEssayText() {
        return essayText;
    }

    public void setEssayText(String essayText) {
        this.essayText = essayText;
    }

    public QuestionDTO getSpeakingQuestion() {
        return speakingQuestion;
    }

    public void setSpeakingQuestion(QuestionDTO speakingQuestion) {
        this.speakingQuestion = speakingQuestion;
    }

    public String getSpeakingUrl() {
        return speakingUrl;
    }

    public void setSpeakingUrl(String speakingUrl) {
        this.speakingUrl = speakingUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<CoachAnswerDTO> getCoachAnswers() {
        return coachAnswers;
    }

    public void setCoachAnswers(List<CoachAnswerDTO> coachAnswers) {
        this.coachAnswers = coachAnswers;
    }

    public static class Builder {
        private final TestVerificationDTO dto;

        public Builder() {
            dto = new TestVerificationDTO();
        }

        public Builder testId(Long testId) {
            dto.testId = testId;
            return this;
        }

        public Builder testLevel(String testLevel) {
            dto.testLevel = testLevel;
            return this;
        }

        public Builder reportedQuestions(List<ReportedQuestionDTO> reportedQuestions) {
            dto.reportedQuestions = reportedQuestions;
            return this;
        }

        public Builder essayQuestion(QuestionDTO essayQuestion) {
            dto.essayQuestion = essayQuestion;
            return this;
        }

        public Builder essayText(String essayText) {
            dto.essayText = essayText;
            return this;
        }

        public Builder speakingQuestion(QuestionDTO speakingQuestion) {
            dto.speakingQuestion = speakingQuestion;
            return this;
        }

        public Builder speakingUrl(String speakingUrl) {
            dto.speakingUrl = speakingUrl;
            return this;
        }

        public Builder grades(List<CoachGradeDTO> gradesDTO) {
            dto.grades = gradesDTO;
            return this;
        }

        public Builder coachAnswers(List<CoachAnswerDTO> coachAnswersDTO) {
            dto.coachAnswers = coachAnswersDTO;
            return this;
        }

        public TestVerificationDTO build() {
            return dto;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestVerificationDTO that = (TestVerificationDTO) o;
        return Objects.equals(testId, that.testId)
               && Objects.equals(testLevel, that.testLevel)
               && Objects.equals(reportedQuestions, that.reportedQuestions)
               && Objects.equals(essayQuestion, that.essayQuestion)
               && Objects.equals(essayText, that.essayText)
               && Objects.equals(speakingQuestion, that.speakingQuestion)
               && Objects.equals(speakingUrl, that.speakingUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, testLevel, reportedQuestions,
                essayQuestion, essayText, speakingQuestion, speakingUrl);
    }
}
