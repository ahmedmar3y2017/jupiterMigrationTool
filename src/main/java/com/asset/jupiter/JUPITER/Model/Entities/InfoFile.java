package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.sql.Timestamp;


/**
 * The persistent class for the INFO_FILES database table.
 */
@Entity
@Table(name = "MG_INFO_FILES")
public class InfoFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "INFO_ID", unique = true, nullable = false, precision = 38)
    private long infoId;

    @Column(name = "\"DESC\"", length = 500)
    private String desc;

    @Column(name = "FINISH_STATUS", length = 100)
    private String finishStatus;

    @Column(name = "\"PATH\"", length = 255)
    private String path;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ADDING_DATE")
    private Timestamp addingDate;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PROCESSING_DATE")
    private Timestamp processingDate;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISH_DATE")
    private Timestamp finishDate;

    @Column(length = 100)
    private String status;

    public InfoFile() {
    }

    public InfoFile(String desc,
                    String finishStatus,
                    String path,
                    Timestamp addingDate,
                    Timestamp processingDate,
                    Timestamp finishDate,
                    String status) {
        this.desc = desc;
        this.finishStatus = finishStatus;
        this.path = path;
        this.addingDate = addingDate;
        this.processingDate = processingDate;
        this.finishDate = finishDate;
        this.status = status;
    }

    public long getInfoId() {
        return this.infoId;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFinishStatus() {
        return this.finishStatus;
    }

    public void setFinishStatus(String finishStatus) {
        this.finishStatus = finishStatus;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Timestamp getProcessingDate() {
        return this.processingDate;
    }

    public void setProcessingDate(Timestamp processingDate) {
        this.processingDate = processingDate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(Timestamp addingDate) {
        this.addingDate = addingDate;
    }

    public Timestamp getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Timestamp finishDate) {
        this.finishDate = finishDate;
    }

    @Override
    public String toString() {
        return "InfoFile{" +
                "infoId=" + infoId +
                ", desc='" + desc + '\'' +
                ", finishStatus='" + finishStatus + '\'' +
                ", path='" + path + '\'' +
                ", addingDate=" + addingDate +
                ", processingDate=" + processingDate +
                ", finishDate=" + finishDate +
                ", status='" + status + '\'' +
                '}';
    }
}