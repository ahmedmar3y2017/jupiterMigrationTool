package com.asset.jupiter.Web.JasperReports;

import java.math.BigDecimal;
import java.sql.Timestamp;

// report Mail
public class reportMailModel {

    private String path;
    private String threadName;
    private String status;
    private Timestamp addingDate;
    private String type;
    private BigDecimal countFolders;
    private BigDecimal countDocuments;

    public reportMailModel() {
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Timestamp getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(Timestamp addingDate) {
        this.addingDate = addingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getCountFolders() {
        return countFolders;
    }

    public void setCountFolders(BigDecimal countFolders) {
        this.countFolders = countFolders;
    }

    public BigDecimal getCountDocuments() {
        return countDocuments;
    }

    public void setCountDocuments(BigDecimal countDocuments) {
        this.countDocuments = countDocuments;
    }
}
