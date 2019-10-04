package com.asset.jupiter.JUPITERMEDIA.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the MEDIAMANAGER database table.
 */
@Entity
@NamedQuery(name = "Mediamanager.findAll", query = "SELECT m FROM Mediamanager m")
public class Mediamanager implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MediamanagerPK id;

    @Column(name = "CREATION_DATE")
    private String creationDate;

    private BigDecimal documentsize;

    private String ext;

    private String iscompressed;

    private String isencrypted;

    private String locationid;

    @Column(name = "MULTI_PAGE_COUNT")
    private BigDecimal multiPageCount;

    public Mediamanager() {
    }

    public Mediamanager(MediamanagerPK id,
                        String creationDate,
                        BigDecimal documentsize,
                        String ext,
                        String iscompressed,
                        String isencrypted,
                        String locationid,
                        BigDecimal multiPageCount) {
        this.id = id;
        this.creationDate = creationDate;
        this.documentsize = documentsize;
        this.ext = ext;
        this.iscompressed = iscompressed;
        this.isencrypted = isencrypted;
        this.locationid = locationid;
        this.multiPageCount = multiPageCount;
    }

    public MediamanagerPK getId() {
        return this.id;
    }

    public void setId(MediamanagerPK id) {
        this.id = id;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getDocumentsize() {
        return this.documentsize;
    }

    public void setDocumentsize(BigDecimal documentsize) {
        this.documentsize = documentsize;
    }

    public String getExt() {
        return this.ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getIscompressed() {
        return this.iscompressed;
    }

    public void setIscompressed(String iscompressed) {
        this.iscompressed = iscompressed;
    }

    public String getIsencrypted() {
        return this.isencrypted;
    }

    public void setIsencrypted(String isencrypted) {
        this.isencrypted = isencrypted;
    }

    public String getLocationid() {
        return this.locationid;
    }

    public void setLocationid(String locationid) {
        this.locationid = locationid;
    }

    public BigDecimal getMultiPageCount() {
        return this.multiPageCount;
    }

    public void setMultiPageCount(BigDecimal multiPageCount) {
        this.multiPageCount = multiPageCount;
    }

    @Override
    public String toString() {
        return "Mediamanager{" +
                "id=" + id +
                ", creationDate='" + creationDate + '\'' +
                ", documentsize=" + documentsize +
                ", ext='" + ext + '\'' +
                ", iscompressed='" + iscompressed + '\'' +
                ", isencrypted='" + isencrypted + '\'' +
                ", locationid='" + locationid + '\'' +
                ", multiPageCount=" + multiPageCount +
                '}';
    }
}