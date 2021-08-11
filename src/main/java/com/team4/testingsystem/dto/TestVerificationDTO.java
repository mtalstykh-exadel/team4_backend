package com.team4.testingsystem.dto;

import java.io.Serializable;
import java.util.List;

public class TestVerificationDTO implements Serializable {
    private Long testId;
    private List<ReportedQuestionDTO> reportedQuestions;
    private QuestionDTO essayQuestion;
    private String essayText;
    private QuestionDTO speakingQuestion;
    private String speakingUrl;

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
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

    public static class Builder {
        private final TestVerificationDTO dto;

        public Builder() {
            dto = new TestVerificationDTO();
        }

        public Builder testId(Long testId) {
            dto.testId = testId;
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

        public TestVerificationDTO build() {
            return dto;
        }
    }
}
