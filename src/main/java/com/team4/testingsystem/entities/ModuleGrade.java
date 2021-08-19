package com.team4.testingsystem.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "module_grade")
public class ModuleGrade implements Serializable {

    @EmbeddedId
    private TestModuleID id;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "coach_comment")
    private String coachComment;

    public ModuleGrade() {
    }

    public ModuleGrade(TestModuleID id, Integer grade, String coachComment) {
        this.id = id;
        this.grade = grade;
        this.coachComment = coachComment;
    }

    public TestModuleID getId() {
        return id;
    }

    public void setId(TestModuleID id) {
        this.id = id;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getCoachComment() {
        return coachComment;
    }

    public void setCoachComment(String coachComment) {
        this.coachComment = coachComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModuleGrade that = (ModuleGrade) o;
        return Objects.equals(id, that.id)
                && Objects.equals(grade, that.grade)
                && Objects.equals(coachComment, that.coachComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grade, coachComment);
    }

}
