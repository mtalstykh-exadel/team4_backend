package com.team4.testingsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "chosen_option")
public class ChosenOption implements Serializable {

    @EmbeddedId
    TestQuestionID id;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "chosen_answer_id", referencedColumnName = "id")
    private Answer answer;

    public ChosenOption() {
    }

    public ChosenOption(TestQuestionID id, Answer answer) {
        this.id = id;
        this.answer = answer;
    }

    public TestQuestionID getId() {
        return id;
    }

    public void setId(TestQuestionID id) {
        this.id = id;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChosenOption that = (ChosenOption) o;
        return id.equals(that.id) && answer.equals(that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer);
    }
}
