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
    ErrorReportId errorReportId;

    @Column(name = "report_body")
    private String reportBody;

    public ErrorReport(){
    }

    public ErrorReport(ErrorReportId errorReportId, String reportBody) {
        this.errorReportId = errorReportId;
        this.reportBody = reportBody;
    }


    public ErrorReportId getErrorReportId() {
        return errorReportId;
    }

    public void setErrorReportId(ErrorReportId errorReportId) {
        this.errorReportId = errorReportId;
    }

    public String getReportBody() {
        return reportBody;
    }

    public void setReportBody(String reportBody) {
        this.reportBody = reportBody;
    }


}
