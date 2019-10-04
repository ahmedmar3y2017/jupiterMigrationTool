package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * The persistent class for the INDEXCLASS database table.
 * 
 */
@Entity
@Table(name="INDEXCLASS")
@NamedQuery(name="Indexclass.findAll", query="SELECT i FROM Indexclass i")
public class Indexclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=16)
	private String classid;

	@Column(length=1)
	private String allowaccessrightsinheritance;

	@Column(nullable=false, length=256)
	private String classname;

	@Column(name="CREATE_REVISIONS", length=1)
	private String createRevisions;

	@Column(length=16)
	private String defviewid;

	@Column(name="DOMAIN_ID", precision=6)
	private BigDecimal domainId;

	@Column(nullable=false, length=16)
	private String labelfieldid;

	@Column(name="LIST_VIEWS_ID", precision=6)
	private BigDecimal listViewsId;

	@Column(name="MAX_MAJOR_REVISIONS", precision=12)
	private BigDecimal maxMajorRevisions;

	@Column(name="MAX_MINOR_REVISIONS", precision=12)
	private BigDecimal maxMinorRevisions;

	@Column(nullable=false, length=10)
	private String mediaserverid;

	@Column(name="NOTIFY_REQUEST_APPROVAL", length=1)
	private String notifyRequestApproval;

	@Column(name="NOTIFY_REVISION_REJECTION", length=1)
	private String notifyRevisionRejection;

	@Column(nullable=false, length=16)
	private String primarytableid;

	public Indexclass() {
	}

	public String getClassid() {
		return this.classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getAllowaccessrightsinheritance() {
		return this.allowaccessrightsinheritance;
	}

	public void setAllowaccessrightsinheritance(String allowaccessrightsinheritance) {
		this.allowaccessrightsinheritance = allowaccessrightsinheritance;
	}

	public String getClassname() {
		return this.classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getCreateRevisions() {
		return this.createRevisions;
	}

	public void setCreateRevisions(String createRevisions) {
		this.createRevisions = createRevisions;
	}

	public String getDefviewid() {
		return this.defviewid;
	}

	public void setDefviewid(String defviewid) {
		this.defviewid = defviewid;
	}

	public BigDecimal getDomainId() {
		return this.domainId;
	}

	public void setDomainId(BigDecimal domainId) {
		this.domainId = domainId;
	}

	public String getLabelfieldid() {
		return this.labelfieldid;
	}

	public void setLabelfieldid(String labelfieldid) {
		this.labelfieldid = labelfieldid;
	}

	public BigDecimal getListViewsId() {
		return this.listViewsId;
	}

	public void setListViewsId(BigDecimal listViewsId) {
		this.listViewsId = listViewsId;
	}

	public BigDecimal getMaxMajorRevisions() {
		return this.maxMajorRevisions;
	}

	public void setMaxMajorRevisions(BigDecimal maxMajorRevisions) {
		this.maxMajorRevisions = maxMajorRevisions;
	}

	public BigDecimal getMaxMinorRevisions() {
		return this.maxMinorRevisions;
	}

	public void setMaxMinorRevisions(BigDecimal maxMinorRevisions) {
		this.maxMinorRevisions = maxMinorRevisions;
	}

	public String getMediaserverid() {
		return this.mediaserverid;
	}

	public void setMediaserverid(String mediaserverid) {
		this.mediaserverid = mediaserverid;
	}

	public String getNotifyRequestApproval() {
		return this.notifyRequestApproval;
	}

	public void setNotifyRequestApproval(String notifyRequestApproval) {
		this.notifyRequestApproval = notifyRequestApproval;
	}

	public String getNotifyRevisionRejection() {
		return this.notifyRevisionRejection;
	}

	public void setNotifyRevisionRejection(String notifyRevisionRejection) {
		this.notifyRevisionRejection = notifyRevisionRejection;
	}

	public String getPrimarytableid() {
		return this.primarytableid;
	}

	public void setPrimarytableid(String primarytableid) {
		this.primarytableid = primarytableid;
	}

	@Override
	public String toString() {
		return "Indexclass{" +
				"classid='" + classid + '\'' +
				", allowaccessrightsinheritance='" + allowaccessrightsinheritance + '\'' +
				", classname='" + classname + '\'' +
				", createRevisions='" + createRevisions + '\'' +
				", defviewid='" + defviewid + '\'' +
				", domainId=" + domainId +
				", labelfieldid='" + labelfieldid + '\'' +
				", listViewsId=" + listViewsId +
				", maxMajorRevisions=" + maxMajorRevisions +
				", maxMinorRevisions=" + maxMinorRevisions +
				", mediaserverid='" + mediaserverid + '\'' +
				", notifyRequestApproval='" + notifyRequestApproval + '\'' +
				", notifyRevisionRejection='" + notifyRevisionRejection + '\'' +
				", primarytableid='" + primarytableid + '\'' +
				'}';
	}
}