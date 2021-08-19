package com.team4.testingsystem.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "coach_answer")
public class CoachAnswer implements Serializable {

    @EmbeddedId
    private TestQuestionID id;

    @Column(name = "comment")
    private String comment;

    public CoachAnswer() {

    }

    public CoachAnswer(TestQuestionID id, String comment) {
        this.id = id;
        this.comment = comment;
    }

    public TestQuestionID getId() {
        return id;
    }

    public void setId(TestQuestionID id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CoachAnswer that = (CoachAnswer) o;
        return Objects.equals(id, that.id)
                && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comment);
    }
}
