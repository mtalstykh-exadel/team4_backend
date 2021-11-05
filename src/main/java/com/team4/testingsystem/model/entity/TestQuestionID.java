package com.team4.testingsystem.model.entity;

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
public class TestQuestionID implements Serializable {

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

}
