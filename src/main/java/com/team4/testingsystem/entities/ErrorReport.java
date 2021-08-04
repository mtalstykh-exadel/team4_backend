package com.team4.testingsystem.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "error_report")
public class ErrorReport implements Serializable {

    @EmbeddedId
    TestQuestionID id;

    @Column(name = "report_body")
    private String reportBody;

    public ErrorReport(){
    }

    public ErrorReport(TestQuestionID id, String reportBody) {
        this.id = id;
        this.reportBody = reportBody;
    }

    public TestQuestionID getId() {
        return id;
    }

    public void setId(TestQuestionID testQuestionID) {
        this.id = testQuestionID;
    }

    public String getReportBody() {
        return reportBody;
    }

    public void setReportBody(String reportBody) {
        this.reportBody = reportBody;
    }
}
