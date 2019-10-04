package com.asset.jupiter.JUPITER.Model.Entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the ITEMS database table.
 */
@Entity
@Table(name = "ITEMS")
@NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i")
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true, nullable = false, length = 16)
    private String itemid;

    @Column(nullable = false, length = 1)
    private String annotated;

    @Column(name = "APPROVAL_STATUS", precision = 1)
    private BigDecimal approvalStatus;

    @Column(name = "BOOKMARK_COUNT", precision = 3)
    private BigDecimal bookmarkCount;

    @Column(nullable = false, precision = 16)
    private BigDecimal creationdate;

    @Column(nullable = false, precision = 16)
    private BigDecimal creator;

    @Column(precision = 16)
    private BigDecimal documentsize;

    @Column(nullable = false, length = 10)
    private String ext;

    @Column(name = "FTR_STATUS", precision = 2)
    private BigDecimal ftrStatus;

    @Column(nullable = false, length = 1)
    private String imagetype;

    @Column(nullable = false, length = 10)
    private String indexclass;

    @Column(precision = 3)
    // ignore this column from hibernate mapping
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal ispdfdigitallysigned;

    @Column(name = "\"LABEL\"", nullable = false, length = 1024)
    private String label;

    @Column(precision = 12)
    private BigDecimal lastmodifiedby;

    @Column(nullable = false, precision = 3)
    private BigDecimal lockcnt;

    @Column(nullable = false, precision = 3)
    private BigDecimal locktype;

    @Column(nullable = false, length = 10)
    private String mediaserverid;

    @Column(nullable = false, precision = 16)
    private BigDecimal moddate;

    @Column(name = "MULTI_PAGE_COUNT", precision = 18)
    private BigDecimal multiPageCount;

    @Column(nullable = false, precision = 16)
    private BigDecimal owner;

    @Column(nullable = false, length = 1)
    private String revised;

    @Column(nullable = false, precision = 3)
    private BigDecimal status;

    @Column(name = "\"TYPE\"", nullable = false, precision = 3)
    private BigDecimal type;

    @Column(nullable = false, precision = 12)
    private BigDecimal usercheckoutid;

    @Column(name = "\"VERSION\"", length = 10)
    private String version;

    //bi-directional many-to-one association to Accesstemplate
    @ManyToOne
    @JoinColumn(name = "ACL_ID")
    private Accesstemplate accesstemplate;

    //bi-directional many-to-one association to ItemIndexclass
    @OneToMany(mappedBy = "item")
    private List<ItemIndexclass> itemIndexclasses;

    public Item() {
    }

    public Item(String itemid, String annotated, BigDecimal approvalStatus,
                BigDecimal bookmarkCount, BigDecimal creationdate, BigDecimal creator,
                BigDecimal documentsize, String ext, BigDecimal ftrStatus, String imagetype,
                String indexclass, BigDecimal ispdfdigitallysigned, String label,
                BigDecimal lastmodifiedby, BigDecimal lockcnt, BigDecimal locktype,
                String mediaserverid, BigDecimal moddate, BigDecimal multiPageCount,
                BigDecimal owner, String revised, BigDecimal status, BigDecimal type,
                BigDecimal usercheckoutid, String version, Accesstemplate accesstemplate) {
        this.itemid = itemid;
        this.annotated = annotated;
        this.approvalStatus = approvalStatus;
        this.bookmarkCount = bookmarkCount;
        this.creationdate = creationdate;
        this.creator = creator;
        this.documentsize = documentsize;
        this.ext = ext;
        this.ftrStatus = ftrStatus;
        this.imagetype = imagetype;
        this.indexclass = indexclass;
        this.ispdfdigitallysigned = ispdfdigitallysigned;
        this.label = label;
        this.lastmodifiedby = lastmodifiedby;
        this.lockcnt = lockcnt;
        this.locktype = locktype;
        this.mediaserverid = mediaserverid;
        this.moddate = moddate;
        this.multiPageCount = multiPageCount;
        this.owner = owner;
        this.revised = revised;
        this.status = status;
        this.type = type;
        this.usercheckoutid = usercheckoutid;
        this.version = version;
        this.accesstemplate = accesstemplate;
    }

    public String getItemid() {
        return this.itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getAnnotated() {
        return this.annotated;
    }

    public void setAnnotated(String annotated) {
        this.annotated = annotated;
    }

    public BigDecimal getApprovalStatus() {
        return this.approvalStatus;
    }

    public void setApprovalStatus(BigDecimal approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public BigDecimal getBookmarkCount() {
        return this.bookmarkCount;
    }

    public void setBookmarkCount(BigDecimal bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public BigDecimal getCreationdate() {
        return this.creationdate;
    }

    public void setCreationdate(BigDecimal creationdate) {
        this.creationdate = creationdate;
    }

    public BigDecimal getCreator() {
        return this.creator;
    }

    public void setCreator(BigDecimal creator) {
        this.creator = creator;
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

    public BigDecimal getFtrStatus() {
        return this.ftrStatus;
    }

    public void setFtrStatus(BigDecimal ftrStatus) {
        this.ftrStatus = ftrStatus;
    }

    public String getImagetype() {
        return this.imagetype;
    }

    public void setImagetype(String imagetype) {
        this.imagetype = imagetype;
    }

    public String getIndexclass() {
        return this.indexclass;
    }

    public void setIndexclass(String indexclass) {
        this.indexclass = indexclass;
    }

    public BigDecimal getIspdfdigitallysigned() {
        return this.ispdfdigitallysigned;
    }

    public void setIspdfdigitallysigned(BigDecimal ispdfdigitallysigned) {
        this.ispdfdigitallysigned = ispdfdigitallysigned;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getLastmodifiedby() {
        return this.lastmodifiedby;
    }

    public void setLastmodifiedby(BigDecimal lastmodifiedby) {
        this.lastmodifiedby = lastmodifiedby;
    }

    public BigDecimal getLockcnt() {
        return this.lockcnt;
    }

    public void setLockcnt(BigDecimal lockcnt) {
        this.lockcnt = lockcnt;
    }

    public BigDecimal getLocktype() {
        return this.locktype;
    }

    public void setLocktype(BigDecimal locktype) {
        this.locktype = locktype;
    }

    public String getMediaserverid() {
        return this.mediaserverid;
    }

    public void setMediaserverid(String mediaserverid) {
        this.mediaserverid = mediaserverid;
    }

    public BigDecimal getModdate() {
        return this.moddate;
    }

    public void setModdate(BigDecimal moddate) {
        this.moddate = moddate;
    }

    public BigDecimal getMultiPageCount() {
        return this.multiPageCount;
    }

    public void setMultiPageCount(BigDecimal multiPageCount) {
        this.multiPageCount = multiPageCount;
    }

    public BigDecimal getOwner() {
        return this.owner;
    }

    public void setOwner(BigDecimal owner) {
        this.owner = owner;
    }

    public String getRevised() {
        return this.revised;
    }

    public void setRevised(String revised) {
        this.revised = revised;
    }

    public BigDecimal getStatus() {
        return this.status;
    }

    public void setStatus(BigDecimal status) {
        this.status = status;
    }

    public BigDecimal getType() {
        return this.type;
    }

    public void setType(BigDecimal type) {
        this.type = type;
    }

    public BigDecimal getUsercheckoutid() {
        return this.usercheckoutid;
    }

    public void setUsercheckoutid(BigDecimal usercheckoutid) {
        this.usercheckoutid = usercheckoutid;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Accesstemplate getAccesstemplate() {
        return this.accesstemplate;
    }

    public void setAccesstemplate(Accesstemplate accesstemplate) {
        this.accesstemplate = accesstemplate;
    }

    public List<ItemIndexclass> getItemIndexclasses() {
        return this.itemIndexclasses;
    }

    public void setItemIndexclasses(List<ItemIndexclass> itemIndexclasses) {
        this.itemIndexclasses = itemIndexclasses;
    }

    public ItemIndexclass addItemIndexclass(ItemIndexclass itemIndexclass) {
        getItemIndexclasses().add(itemIndexclass);
        itemIndexclass.setItem(this);

        return itemIndexclass;
    }

    public ItemIndexclass removeItemIndexclass(ItemIndexclass itemIndexclass) {
        getItemIndexclasses().remove(itemIndexclass);
        itemIndexclass.setItem(null);

        return itemIndexclass;
    }


}