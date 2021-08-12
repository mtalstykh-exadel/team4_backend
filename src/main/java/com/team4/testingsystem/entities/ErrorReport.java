package com.team4.testingsystem.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "error_report")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorReport implements Serializable {

    @EmbeddedId
    TestQuestionID id;

    @Column(name = "report_body")
    private String reportBody;

}
