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
@Table(name = "coach_grade")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoachGrade implements Serializable {

    @EmbeddedId
    private TestQuestionID id;

    @Column(name = "grade")
    private Integer grade;

    @Column (name = "comment")
    private String comment;

}
