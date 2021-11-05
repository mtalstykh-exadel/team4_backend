package com.team4.testingsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "coach_answer")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoachAnswer implements Serializable {

    @EmbeddedId
    private TestQuestionID id;

    @Column(name = "comment")
    private String comment;

}
