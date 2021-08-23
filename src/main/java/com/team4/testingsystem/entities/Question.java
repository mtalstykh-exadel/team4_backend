package com.team4.testingsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@Getter
@Setter
@EqualsAndHashCode
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "question_body")
    private String body;
    @Column(name = "is_available")
    private boolean isAvailable = true;

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

        public Builder answers(List<Answer> answers) {
            question.setAnswers(answers);
            return this;
        }

        public Question build() {
            return question;
        }
    }
}
