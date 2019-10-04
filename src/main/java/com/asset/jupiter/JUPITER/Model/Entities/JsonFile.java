package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * The persistent class for the MG_JSON_FILES database table.
 */
@Entity
@Table(name = "MG_JSON_FILES")
public class JsonFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private JsonFilePK id;

    @Column(name = "COUNT_DOCUMENTS")
    private BigDecimal countDocuments;

    @Column(name = "COUNT_FOLDERS")
    private BigDecimal countFolders;

    @Column(name = "\"TYPE\"")
    private String type;
    @Column(name = "THREAD_NAME")
    private String threadName;

    public JsonFile() {
    }

    public JsonFile(JsonFilePK id, BigDecimal countDocuments,
                    BigDecimal countFolders,
                    String type,
                    String threadName) {
        this.id = id;
        this.countDocuments = countDocuments;
        this.countFolders = countFolders;
        this.type = type;
        this.threadName = threadName;
    }

    public JsonFilePK getId() {
        return this.id;
    }

    public void setId(JsonFilePK id) {
        this.id = id;
    }

    public BigDecimal getCountDocuments() {
        return this.countDocuments;
    }

    public void setCountDocuments(BigDecimal countDocuments) {
        this.countDocuments = countDocuments;
    }

    public BigDecimal getCountFolders() {
        return this.countFolders;
    }

    public void setCountFolders(BigDecimal countFolders) {
        this.countFolders = countFolders;
    }

    public String getType() {
        return this.type;
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