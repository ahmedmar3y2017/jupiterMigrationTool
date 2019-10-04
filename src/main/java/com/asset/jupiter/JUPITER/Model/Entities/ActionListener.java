package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * The persistent class for the MG_ACTION_LISTENER database table.
 */
@Entity
@Table(name = "MG_ACTION_LISTENER")
public class ActionListener implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ActionListenerPK id;

    @Column(name = "FAILED_DOCUMENTS")
    private BigDecimal failedDocuments;

    @Column(name = "FAILED_FOLDERS")
    private BigDecimal failedFolders;

    @Column(name = "SUCCESS_DOCUMENTS")
    private BigDecimal successDocuments;

    @Column(name = "SUCCESS_FOLDERS")
    private BigDecimal successFolders;
    @Column(name = "THREAD_NAME")
    private String threadName;

    public ActionListener() {
    }

    public ActionListener(ActionListenerPK id,
                          BigDecimal failedDocuments,
                          BigDecimal failedFolders,
                          BigDecimal successDocuments,
                          BigDecimal successFolders, String threadName
    ) {
        this.id = id;
        this.failedDocuments = failedDocuments;
        this.failedFolders = failedFolders;
        this.successDocuments = successDocuments;
        this.successFolders = successFolders;
        this.threadName = threadName;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public ActionListenerPK getId() {
        return this.id;
    }

    public void setId(ActionListenerPK id) {
        this.id = id;
    }

    public BigDecimal getFailedDocuments() {
        return this.failedDocuments;
    }

    public void setFailedDocuments(BigDecimal failedDocuments) {
        this.failedDocuments = failedDocuments;
    }

    public BigDecimal getFailedFolders() {
        return this.failedFolders;
    }

    public void setFailedFolders(BigDecimal failedFolders) {
        this.failedFolders = failedFolders;
    }

    public BigDecimal getSuccessDocuments() {
        return this.successDocuments;
    }

    public void setSuccessDocuments(BigDecimal successDocuments) {
        this.successDocuments = successDocuments;
    }

    public BigDecimal getSuccessFolders() {
        return this.successFolders;
    }

    public void setSuccessFolders(BigDecimal successFolders) {
        this.successFolders = successFolders;
    }

}