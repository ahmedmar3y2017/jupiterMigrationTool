package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the MG_EXCEPTIONS database table.
 * 
 */
@Embeddable
public class ExceptionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="EXCEPTION_ID")
	private long exceptionId;

	@Column(name="INFO_FILE_ID", insertable=false, updatable=false)
	private long infoFileId;

	public ExceptionPK() {
	}

	public ExceptionPK(long exceptionId, long infoFileId) {
		this.exceptionId = exceptionId;
		this.infoFileId = infoFileId;
	}

	public long getExceptionId() {
		return this.exceptionId;
	}
	public void setExceptionId(long exceptionId) {
		this.exceptionId = exceptionId;
	}
	public long getInfoFileId() {
		return this.infoFileId;
	}
	public void setInfoFileId(long infoFileId) {
		this.infoFileId = infoFileId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ExceptionPK)) {
			return false;
		}
		ExceptionPK castOther = (ExceptionPK)other;
		return 
			(this.exceptionId == castOther.exceptionId)
			&& (this.infoFileId == castOther.infoFileId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.exceptionId ^ (this.exceptionId >>> 32)));
		hash = hash * prime + ((int) (this.infoFileId ^ (this.infoFileId >>> 32)));
		
		return hash;
	}
}