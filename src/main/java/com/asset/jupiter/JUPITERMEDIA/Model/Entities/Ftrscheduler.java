package com.asset.jupiter.JUPITERMEDIA.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FTRSCHEDULER database table.
 */
@Entity
@NamedQuery(name = "Ftrscheduler.findAll", query = "SELECT f FROM Ftrscheduler f")
public class Ftrscheduler implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String operationid;

    private BigDecimal creationdate;

    private String ext;

    private BigDecimal ftroperation;

    private String ifsschemapassword;

    private String indexclassdata;

    private String info;

    private String locationid;

    private String mediaaddress;

    private String mediapassword;

    private String mediaserverid;

    private BigDecimal mediaservertype;

    private String mediausername;

    private BigDecimal moddate;

    private String objectid;

    private BigDecimal objecttype;

    private BigDecimal status;

    public Ftrscheduler() {
    }

    public Ftrscheduler(String operationid,
                        BigDecimal creationdate,
                        String ext,
                        BigDecimal ftroperation,
                        String ifsschemapassword,
                        String indexclassdata,
                        String info,
                        String locationid,
                        String mediaaddress,
                        String mediapassword,
                        String mediaserverid,
                        BigDecimal mediaservertype,
                        String mediausername,
                        BigDecimal moddate,
                        String objectid,
                        BigDecimal objecttype,
                        BigDecimal status
    ) {
        this.operationid = operationid;
        this.creationdate = creationdate;
        this.ext = ext;
        this.ftroperation = ftroperation;
        this.ifsschemapassword = ifsschemapassword;
        this.indexclassdata = indexclassdata;
        this.info = info;
        this.locationid = locationid;
        this.mediaaddress = mediaaddress;
        this.mediapassword = mediapassword;
        this.mediaserverid = mediaserverid;
        this.mediaservertype = mediaservertype;
        this.mediausername = mediausername;
        this.moddate = moddate;
        this.objectid = objectid;
        this.objecttype = objecttype;
        this.status = status;
    }

    public String getOperationid() {
        return this.operationid;
    }

    public void setOperationid(String operationid) {
        this.operationid = operationid;
    }

    public BigDecimal getCreationdate() {
        return this.creationdate;
    }

    public void setCreationdate(BigDecimal creationdate) {
        this.creationdate = creationdate;
    }

    public String getExt() {
        return this.ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public BigDecimal getFtroperation() {
        return this.ftroperation;
    }

    public void setFtroperation(BigDecimal ftroperation) {
        this.ftroperation = ftroperation;
    }

    public String getIfsschemapassword() {
        return this.ifsschemapassword;
    }

    public void setIfsschemapassword(String ifsschemapassword) {
        this.ifsschemapassword = ifsschemapassword;
    }

    public String getIndexclassdata() {
        return this.indexclassdata;
    }

    public void setIndexclassdata(String indexclassdata) {
        this.indexclassdata = indexclassdata;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLocationid() {
        return this.locationid;
    }

    public void setLocationid(String locationid) {
        this.locationid = locationid;
    }

    public String getMediaaddress() {
        return this.mediaaddress;
    }

    public void setMediaaddress(String mediaaddress) {
        this.mediaaddress = mediaaddress;
    }

    public String getMediapassword() {
        return this.mediapassword;
    }

    public void setMediapassword(String mediapassword) {
        this.mediapassword = mediapassword;
    }

    public String getMediaserverid() {
        return this.mediaserverid;
    }

    public void setMediaserverid(String mediaserverid) {
        this.mediaserverid = mediaserverid;
    }

    public BigDecimal getMediaservertype() {
        return this.mediaservertype;
    }

    public void setMediaservertype(BigDecimal mediaservertype) {
        this.mediaservertype = mediaservertype;
    }

    public String getMediausername() {
        return this.mediausername;
    }

    public void setMediausername(String mediausername) {
        this.mediausername = mediausername;
    }

    public BigDecimal getModdate() {
        return this.moddate;
    }

    public void setModdate(BigDecimal moddate) {
        this.moddate = moddate;
    }

    public String getObjectid() {
        return this.objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public BigDecimal getObjecttype() {
        return this.objecttype;
    }

    public void setObjecttype(BigDecimal objecttype) {
        this.objecttype = objecttype;
    }

    public BigDecimal getStatus() {
        return this.status;
    }

    public void setStatus(BigDecimal status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ftrscheduler{" +
                "operationid='" + operationid + '\'' +
                ", creationdate=" + creationdate +
                ", ext='" + ext + '\'' +
                ", ftroperation=" + ftroperation +
                ", ifsschemapassword='" + ifsschemapassword + '\'' +
                ", indexclassdata='" + indexclassdata + '\'' +
                ", info='" + info + '\'' +
                ", locationid='" + locationid + '\'' +
                ", mediaaddress='" + mediaaddress + '\'' +
                ", mediapassword='" + mediapassword + '\'' +
                ", mediaserverid='" + mediaserverid + '\'' +
                ", mediaservertype=" + mediaservertype +
                ", mediausername='" + mediausername + '\'' +
                ", moddate=" + moddate +
                ", objectid='" + objectid + '\'' +
                ", objecttype=" + objecttype +
                ", status=" + status +
                '}';
    }
}