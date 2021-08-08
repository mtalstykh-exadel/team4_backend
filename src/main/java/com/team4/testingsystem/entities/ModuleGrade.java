package com.team4.testingsystem.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

@Entity
@Table(name = "module_grade")
public class ModuleGrade implements Serializable{

    @EmbeddedId
    TestModuleID id;

    @Column(name = "grade")
    private Integer grade;

    public ModuleGrade() {
    }

    public ModuleGrade(TestModuleID id, Integer grade) {
        this.id = id;
        this.grade = grade;
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
                && Objects.equals(grade, that.grade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grade);
    }
}
