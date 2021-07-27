package com.team4.testingsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "chosen_option")
public class ChosenOption {

    @EmbeddedId
    ChosenOptionID chosenOptionID;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "chosen_answer_id", referencedColumnName = "id")
    private Answer answer;

    public ChosenOption() {

    }

    public ChosenOption(ChosenOptionID chosenOptionID, Answer answer) {
        this.chosenOptionID = chosenOptionID;
        this.answer = answer;
    }

    public ChosenOptionID getChosenOptionID() {
        return chosenOptionID;
    }

    public void setChosenOptionID(ChosenOptionID chosenOptionID) {
        this.chosenOptionID = chosenOptionID;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChosenOption that = (ChosenOption) o;
        return chosenOptionID.equals(that.chosenOptionID) && answer.equals(that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chosenOptionID, answer);
    }
}

