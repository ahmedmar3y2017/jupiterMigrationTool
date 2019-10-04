package com.asset.jupiter.Web.DTO;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class staticsDTO {


    // info File Data
    private long infoId;

    private String finishStatus;

    private String path;

    private String fileName;

    private Timestamp addingDate;

    private Timestamp processingDate;

    private Timestamp finishDate;

    private String status;

    // Json File Data

    private BigDecimal countDocuments;

    private BigDecimal countFolders;

    private String type;

    private String threadName;

    public staticsDTO() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getInfoId() {
        return infoId;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }


    public String getFinishStatus() {
        return finishStatus;
    }

    public void setFinishStatus(String finishStatus) {
        this.finishStatus = finishStatus;
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

    public Timestamp getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(Timestamp processingDate) {
        this.processingDate = processingDate;
    }

    public Timestamp getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Timestamp finishDate) {
        this.finishDate = finishDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getCountDocuments() {
        return countDocuments;
    }

    public void setCountDocuments(BigDecimal countDocuments) {
        this.countDocuments = countDocuments;
    }

    public BigDecimal getCountFolders() {
        return countFolders;
    }

    public void setCountFolders(BigDecimal countFolders) {
        this.countFolders = countFolders;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
