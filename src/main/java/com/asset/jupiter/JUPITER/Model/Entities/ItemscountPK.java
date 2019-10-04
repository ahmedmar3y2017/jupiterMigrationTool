package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ITEMSCOUNT database table.
 */
@Embeddable
public class ItemscountPK implements Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, length = 16)
    private String parentid;

    @Column(unique = true, nullable = false, precision = 1)
    private long parenttype;

    @Column(unique = true, nullable = false, length = 16)
    private String classid;

    public ItemscountPK() {
    }

    public ItemscountPK(String parentid, long parenttype, String classid) {
        this.parentid = parentid;
        this.parenttype = parenttype;
        this.classid = classid;
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

    public String getClassid() {
        return this.classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ItemscountPK)) {
            return false;
        }
        ItemscountPK castOther = (ItemscountPK) other;
        return
                this.parentid.equals(castOther.parentid)
                        && (this.parenttype == castOther.parenttype)
                        && this.classid.equals(castOther.classid);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.parentid.hashCode();
        hash = hash * prime + ((int) (this.parenttype ^ (this.parenttype >>> 32)));
        hash = hash * prime + this.classid.hashCode();

        return hash;
    }

    @Override
    public String toString() {
        return "ItemscountPK{" +
                "parentid='" + parentid + '\'' +
                ", parenttype=" + parenttype +
                ", classid='" + classid + '\'' +
                '}';
    }
}