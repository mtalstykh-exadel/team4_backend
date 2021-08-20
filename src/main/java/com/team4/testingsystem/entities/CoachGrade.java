package com.team4.testingsystem.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "coach_grade")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CoachGrade implements Serializable {

    @EmbeddedId
    private TestQuestionID id;

    @Column(name = "grade")
    private Integer grade;

    @Column (name = "comment")
    private String comment;

}
