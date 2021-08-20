package com.team4.testingsystem.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "module_coach_answer")
public class ModuleCoachAnswer implements Serializable {

    @EmbeddedId
    private TestQuestionModuleID id;

    @Column(name = "coach_comment")
    private String coachComment;


    public ModuleCoachAnswer(TestQuestionModuleID id, String coachComment) {
        this.id = id;
        this.coachComment = coachComment;
    }

    public ModuleCoachAnswer(Module module, CoachAnswer coachAnswer) {
        id = new TestQuestionModuleID(coachAnswer.getId(), module);
        coachComment = coachAnswer.getComment();
    }

    public ModuleCoachAnswer() {

    }

    public TestQuestionModuleID getId() {
        return id;
    }

    public void setId(TestQuestionModuleID id) {
        this.id = id;
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
        ModuleCoachAnswer that = (ModuleCoachAnswer) o;
        return Objects.equals(id, that.id)
                && Objects.equals(coachComment, that.coachComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, coachComment);
    }
}
