package com.asset.jupiter.JUPITER.Model.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the ACCESSTEMPLATES database table.
 */
@Entity
@Table(name = "ACCESSTEMPLATES")
@NamedQuery(name = "Accesstemplate.findAll", query = "SELECT a FROM Accesstemplate a")
public class Accesstemplate implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @Column(unique = true, nullable = false, precision = 3)
    private long templateid;

    @Column(precision = 3)
    private BigDecimal defaultaccess;

    @Column(name = "DOMAIN_ID", precision = 6)
    private BigDecimal domainId;

    @Column(name = "DYNAMIC_OWNER", precision = 3)
    private BigDecimal dynamicOwner;

    @Column(precision = 12)
    private BigDecimal ownerid;

    @Column(length = 32)
    private String templatename;

    //bi-directional many-to-one association to Item
    @OneToMany(mappedBy = "accesstemplate")

//	@JsonIgnore
    private List<Item> items;

    public Accesstemplate() {
    }

    public Accesstemplate(long templateid,
                          BigDecimal defaultaccess,
                          BigDecimal domainId,
                          BigDecimal dynamicOwner,
                          BigDecimal ownerid,
                          String templatename
    ) {
        this.templateid = templateid;
        this.defaultaccess = defaultaccess;
        this.domainId = domainId;
        this.dynamicOwner = dynamicOwner;
        this.ownerid = ownerid;
        this.templatename = templatename;
    }

    public long getTemplateid() {
        return this.templateid;
    }

    public void setTemplateid(long templateid) {
        this.templateid = templateid;
    }

    public BigDecimal getDefaultaccess() {
        return this.defaultaccess;
    }

    public void setDefaultaccess(BigDecimal defaultaccess) {
        this.defaultaccess = defaultaccess;
    }

    public BigDecimal getDomainId() {
        return this.domainId;
    }

    public void setDomainId(BigDecimal domainId) {
        this.domainId = domainId;
    }

    public BigDecimal getDynamicOwner() {
        return this.dynamicOwner;
    }

    public void setDynamicOwner(BigDecimal dynamicOwner) {
        this.dynamicOwner = dynamicOwner;
    }

    public BigDecimal getOwnerid() {
        return this.ownerid;
    }

    public void setOwnerid(BigDecimal ownerid) {
        this.ownerid = ownerid;
    }

    public String getTemplatename() {
        return this.templatename;
    }

    public void setTemplatename(String templatename) {
        this.templatename = templatename;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Item addItem(Item item) {
        getItems().add(item);
        item.setAccesstemplate(this);

        return item;
    }

    public Item removeItem(Item item) {
        getItems().remove(item);
        item.setAccesstemplate(null);

        return item;
    }


}