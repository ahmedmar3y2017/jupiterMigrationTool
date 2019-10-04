package com.asset.jupiter.JUPITER.Model.Entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * The persistent class for the MEDIASERVERS database table.
 */
@Entity
@Table(name = "MEDIASERVERS")
@NamedQuery(name = "Mediaserver.findAll", query = "SELECT m FROM Mediaserver m")
public class Mediaserver implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true, nullable = false, length = 10)
    private String mediaserverid;

    @Column(length = 300)
    private String address;

    @Column(name = "DOMAIN_ID", precision = 6)
    private BigDecimal domainId;

    @Column(length = 30)
    private String ifsschemapassword;

    @Column(length = 128)
    private String info;

    @Column(precision = 1)
    private BigDecimal mediatype;

    @Column(length = 32)
    private String name;

    @Column(length = 16)
    // ignore this column from hibernate mapping
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @Column(length = 16)
    private String pswd;

    @Column(length = 10)
    private String siteid;

    @Column(length = 16)
    private String username;

    public Mediaserver() {
    }

    public String getMediaserverid() {
        return this.mediaserverid;
    }

    public void setMediaserverid(String mediaserverid) {
        this.mediaserverid = mediaserverid;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getDomainId() {
        return this.domainId;
    }

    public void setDomainId(BigDecimal domainId) {
        this.domainId = domainId;
    }

    public String getIfsschemapassword() {
        return this.ifsschemapassword;
    }

    public void setIfsschemapassword(String ifsschemapassword) {
        this.ifsschemapassword = ifsschemapassword;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public BigDecimal getMediatype() {
        return this.mediatype;
    }

    public void setMediatype(BigDecimal mediatype) {
        this.mediatype = mediatype;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPswd() {
        return this.pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getSiteid() {
        return this.siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}