package com.team4.testingsystem.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "coach_grade")
public class CoachGrade implements Serializable {

    @EmbeddedId
    private TestQuestionID id;

    @Column(name = "grade")
    private Integer grade;

    @Column (name = "comment")
    private String comment;

    public CoachGrade() {
    }

    public CoachGrade(TestQuestionID id, Integer grade, String comment) {
        this.id = id;
        this.grade = grade;
        this.comment = comment;
    }

    public TestQuestionID getId() {
        return id;
    }

    public void setId(TestQuestionID id) {
        this.id = id;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
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
        CoachGrade that = (CoachGrade) o;
        return Objects.equals(id, that.id)
                && Objects.equals(grade, that.grade)
                && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grade, comment);
    }
}
