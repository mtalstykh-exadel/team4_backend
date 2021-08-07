package com.team4.testingsystem.entities;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TestModuleID implements Serializable {

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "module_id", referencedColumnName = "id")
    private Module module;

    public TestModuleID() {
    }

    public TestModuleID(Test test, Module module) {
        this.test = test;
        this.module = module;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
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
        TestModuleID that = (TestModuleID) o;
        return Objects.equals(test, that.test) && Objects.equals(module, that.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(test, module);
    }
}
