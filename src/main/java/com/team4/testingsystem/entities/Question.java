package com.team4.testingsystem.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "question")
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "question_body")
    private String body;
    @Column(name = "is_available")
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "creator_user_id", referencedColumnName = "id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "level_id", referencedColumnName = "id")
    private Level level;

    @ManyToOne
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    private Module module;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    List<Answer> answers = new ArrayList<>();

    @ManyToMany(mappedBy = "questions")
    List<ContentFile> contentFiles = new ArrayList<>();

    @ManyToMany(mappedBy = "questions")
    List<Test> tests = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return isAvailable == question.isAvailable
                && Objects.equals(id, question.id)
                && Objects.equals(body, question.body)
                && Objects.equals(creator, question.creator)
                && Objects.equals(level, question.level)
                && Objects.equals(module, question.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, body, isAvailable, creator, level, module);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<ContentFile> getContentFiles() {
        return contentFiles;
    }

    public void setContentFiles(List<ContentFile> contentFiles) {
        this.contentFiles = contentFiles;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Question question;

        public Builder() {
            this.question = new Question();
        }

        public Builder id(Long id) {
            question.id = id;
            return this;
        }

        public Builder body(String body) {
            question.body = body;
            return this;
        }

        public Builder isAvailable(boolean isAvailable) {
            question.isAvailable = isAvailable;
            return this;
        }

        public Builder creator(User creator) {
            question.creator = creator;
            return this;
        }

        public Builder level(Level level) {
            question.level = level;
            return this;
        }

        public Builder module(Module module) {
            question.module = module;
            return this;
        }

        public Builder answers(List<Answer> answers){
            question.setAnswers(answers);
            return this;
        }

        public Question build() {
            return question;
        }
    }
}
