package com.team4.testingsystem.dto;

public class TestsRequest {

    private String createdAt;
    private String updatedAt;
    private String startedAt;
    private String finishedAt;
    private String status;
    private int evaluation;

    private TestsRequest(){

    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public String getStatus() {
        return status;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public static Builder builder() {
        return new TestsRequest().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Builder setCreatedAt(String createdAt){
            TestsRequest.this.createdAt = createdAt;
            return this;
        }

        public Builder setUpdatedAt(String updatedAt){
            TestsRequest.this.updatedAt = updatedAt;
            return this;
        }

        public Builder setStartedAt(String startedAt){
            TestsRequest.this.startedAt = startedAt;
            return this;
        }

        public Builder setFinishedAt(String finishedAt){
            TestsRequest.this.finishedAt = finishedAt;
            return this;
        }

        public Builder setStatus(String status){
            TestsRequest.this.status = status;
            return this;
        }

        public Builder setEvaluation(int evaluation){
            TestsRequest.this.evaluation = evaluation;
            return this;
        }

        public TestsRequest build() {
            return TestsRequest.this;
        }
    }

    }
