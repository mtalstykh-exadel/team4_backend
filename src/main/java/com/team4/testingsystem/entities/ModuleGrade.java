package com.team4.testingsystem.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "module_grade")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModuleGrade implements Serializable {

    @EmbeddedId
    private TestModuleID id;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "coach_comment")
    private String coachComment;

}
