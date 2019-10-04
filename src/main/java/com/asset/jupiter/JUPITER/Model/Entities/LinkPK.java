package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the LINKS database table.
 * 
 */
@Embeddable
public class LinkPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(unique=true, nullable=false, length=16)
	private String itemid;

	@Column(unique=true, nullable=false, length=16)
	private String parentid;

	@Column(unique=true, nullable=false, precision=1)
	private long parenttype;

	public LinkPK() {
	}

	public LinkPK(String itemid, String parentid, long parenttype) {
		this.itemid = itemid;
		this.parentid = parentid;
		this.parenttype = parenttype;
	}

	public String getItemid() {
		return this.itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public String getParentid() {
		return this.parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public long getParenttype() {
		return this.parenttype;
	}
	public void setParenttype(long parenttype) {
		this.parenttype = parenttype;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof LinkPK)) {
			return false;
		}
		LinkPK castOther = (LinkPK)other;
		return 
			this.itemid.equals(castOther.itemid)
			&& this.parentid.equals(castOther.parentid)
			&& (this.parenttype == castOther.parenttype);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.itemid.hashCode();
		hash = hash * prime + this.parentid.hashCode();
		hash = hash * prime + ((int) (this.parenttype ^ (this.parenttype >>> 32)));
		
		return hash;
	}

	@Override
	public String toString() {
		return "LinkPK{" +
				"itemid='" + itemid + '\'' +
				", parentid='" + parentid + '\'' +
				", parenttype=" + parenttype +
				'}';
	}
}