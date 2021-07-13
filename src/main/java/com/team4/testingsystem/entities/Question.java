package com.team4.testingsystem.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "question_body")
    private String questionBody;
    @Column(name = "is_available")
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Level level;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Module module;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
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
    public String toString() {
        return "Question{" +
               "id=" + id +
               ", questionBody='" + questionBody + '\'' +
               ", isAvailable=" + isAvailable +
               ", creator=" + creator +
               ", level=" + level +
               ", module=" + module +
               '}';
    }

    public static class Builder {
        private Question question;

        public Builder() {
            this.question = new Question();
        }

        public Builder id(Long id) {
            question.id = id;
            return this;
        }

        public Builder questionBody(String questionBody) {
            question.questionBody = questionBody;
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

        public Question build() {
            return question;
        }
    }
}
