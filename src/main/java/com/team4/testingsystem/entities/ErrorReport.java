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
@Table(name = "error_report")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorReport implements Serializable {

    @EmbeddedId
    TestQuestionID id;

    @Column(name = "report_body")
    private String reportBody;

}
