package com.team4.testingsystem.dto;

public class AnswerRequest {
    private String answerBody;
    private long questionId;
    private boolean isCorrect;

    public String getAnswerBody() {
        return answerBody;
    }

    public void setAnswerBody(String answerBody) {
        this.answerBody = answerBody;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final AnswerRequest answerRequest;

        public Builder() {
            answerRequest = new AnswerRequest();
        }

        public Builder answerBody(String answerBody) {
            answerRequest.answerBody = answerBody;
            return this;
        }

        public Builder questionId(long questionId) {
            answerRequest.questionId = questionId;
            return this;
        }

        public Builder isCorrect(boolean isCorrect) {
            answerRequest.isCorrect = isCorrect;
            return this;
        }

        public AnswerRequest build() {
            return answerRequest;
        }
    }
}
