package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the VIEWFIELDS database table.
 * 
 */
@Embeddable
public class ViewfieldPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String viewid;

	private String fieldid;

	public ViewfieldPK() {
	}

	public ViewfieldPK(String viewid, String fieldid) {
		this.viewid = viewid;
		this.fieldid = fieldid;
	}

	public String getViewid() {
		return this.viewid;
	}
	public void setViewid(String viewid) {
		this.viewid = viewid;
	}
	public String getFieldid() {
		return this.fieldid;
	}
	public void setFieldid(String fieldid) {
		this.fieldid = fieldid;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ViewfieldPK)) {
			return false;
		}
		ViewfieldPK castOther = (ViewfieldPK)other;
		return 
			this.viewid.equals(castOther.viewid)
			&& this.fieldid.equals(castOther.fieldid);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.viewid.hashCode();
		hash = hash * prime + this.fieldid.hashCode();
		
		return hash;
	}
}