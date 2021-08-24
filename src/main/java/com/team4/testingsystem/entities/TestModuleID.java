package com.team4.testingsystem.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TestModuleID implements Serializable {

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "module_id", referencedColumnName = "id")
    private Module module;

}
