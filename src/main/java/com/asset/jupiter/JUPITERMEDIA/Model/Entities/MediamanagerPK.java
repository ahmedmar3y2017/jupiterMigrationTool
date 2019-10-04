package com.asset.jupiter.JUPITERMEDIA.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the MEDIAMANAGER database table.
 * 
 */
@Embeddable
public class MediamanagerPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String objectid;

	private long objecttype;

	public MediamanagerPK() {
	}

	public MediamanagerPK(String objectid, long objecttype) {
		this.objectid = objectid;
		this.objecttype = objecttype;
	}

	public String getObjectid() {
		return this.objectid;
	}
	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}
	public long getObjecttype() {
		return this.objecttype;
	}
	public void setObjecttype(long objecttype) {
		this.objecttype = objecttype;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MediamanagerPK)) {
			return false;
		}
		MediamanagerPK castOther = (MediamanagerPK)other;
		return 
			this.objectid.equals(castOther.objectid)
			&& (this.objecttype == castOther.objecttype);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.objectid.hashCode();
		hash = hash * prime + ((int) (this.objecttype ^ (this.objecttype >>> 32)));
		
		return hash;
	}

	@Override
	public String toString() {
		return "MediamanagerPK{" +
				"objectid='" + objectid + '\'' +
				", objecttype=" + objecttype +
				'}';
	}
}