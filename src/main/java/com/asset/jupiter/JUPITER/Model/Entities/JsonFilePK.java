package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the MG_JSON_FILES database table.
 */
@Embeddable
public class JsonFilePK implements Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "\"PATH\"")
    private String path;

    @Column(name = "INFO_FILE_ID", insertable = false, updatable = false)
    private long infoFileId;

    public JsonFilePK() {
    }

    public JsonFilePK(String path, long infoFileId) {
        this.path = path;
        this.infoFileId = infoFileId;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
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
        if (!(other instanceof JsonFilePK)) {
            return false;
        }
        JsonFilePK castOther = (JsonFilePK) other;
        return
                this.path.equals(castOther.path)
                        && (this.infoFileId == castOther.infoFileId);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.path.hashCode();
        hash = hash * prime + ((int) (this.infoFileId ^ (this.infoFileId >>> 32)));

        return hash;
    }
}