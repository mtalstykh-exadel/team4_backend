package com.team4.testingsystem.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "file_answer")
@Getter
@Setter
public class FileAnswer implements Serializable {

    @EmbeddedId
    private TestQuestionID id;

    @Column(name = "url")
    private String url;

    public TestQuestionID getId() {
        return id;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final FileAnswer fileAnswer;

        public Builder() {
            this.fileAnswer = new FileAnswer();
        }

        public Builder id(TestQuestionID id) {
            fileAnswer.id = id;
            return this;
        }

        public Builder url(String url) {
            fileAnswer.url = url;
            return this;
        }

        public FileAnswer build() {
            return fileAnswer;
        }
    }
}
