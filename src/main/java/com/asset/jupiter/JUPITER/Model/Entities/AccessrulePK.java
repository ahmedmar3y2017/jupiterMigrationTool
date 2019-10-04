package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ACCESSRULES database table.
 * 
 */
@Embeddable
public class AccessrulePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(unique=true, nullable=false, length=16)
	private String itemid;

	@Column(unique=true, nullable=false, precision=12)
	private long itemtype;

	@Column(unique=true, nullable=false, precision=12)
	private long ugid;

	@Column(unique=true, nullable=false, precision=1)
	private long ugtype;

	public AccessrulePK() {
	}

	public AccessrulePK(String itemid, long itemtype, long ugid, long ugtype) {
		this.itemid = itemid;
		this.itemtype = itemtype;
		this.ugid = ugid;
		this.ugtype = ugtype;
	}

	public String getItemid() {
		return this.itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public long getItemtype() {
		return this.itemtype;
	}
	public void setItemtype(long itemtype) {
		this.itemtype = itemtype;
	}
	public long getUgid() {
		return this.ugid;
	}
	public void setUgid(long ugid) {
		this.ugid = ugid;
	}
	public long getUgtype() {
		return this.ugtype;
	}
	public void setUgtype(long ugtype) {
		this.ugtype = ugtype;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AccessrulePK)) {
			return false;
		}
		AccessrulePK castOther = (AccessrulePK)other;
		return 
			this.itemid.equals(castOther.itemid)
			&& (this.itemtype == castOther.itemtype)
			&& (this.ugid == castOther.ugid)
			&& (this.ugtype == castOther.ugtype);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.itemid.hashCode();
		hash = hash * prime + ((int) (this.itemtype ^ (this.itemtype >>> 32)));
		hash = hash * prime + ((int) (this.ugid ^ (this.ugid >>> 32)));
		hash = hash * prime + ((int) (this.ugtype ^ (this.ugtype >>> 32)));
		
		return hash;
	}

	@Override
	public String toString() {
		return "AccessrulePK{" +
				"itemid='" + itemid + '\'' +
				", itemtype=" + itemtype +
				", ugid=" + ugid +
				", ugtype=" + ugtype +
				'}';
	}
}