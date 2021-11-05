package com.team4.testingsystem.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "chosen_option")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChosenOption implements Serializable {

    @EmbeddedId
    TestQuestionID id;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "chosen_answer_id", referencedColumnName = "id")
    private Answer answer;
}
