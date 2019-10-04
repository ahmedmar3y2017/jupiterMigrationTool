package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ITEM_INDEXCLASS database table.
 */
@Embeddable
public class ItemIndexclassPK implements Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "ITEM_ID", insertable = false, updatable = false, unique = true, nullable = false, length = 16)
    private String itemId;

    @Column(name = "INDEXCLASS_ID", unique = true, nullable = false, length = 16)
    private String indexclassId;

    public ItemIndexclassPK() {
    }

    public ItemIndexclassPK(String itemId, String indexclassId) {
        this.itemId = itemId;
        this.indexclassId = indexclassId;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getIndexclassId() {
        return this.indexclassId;
    }

    public void setIndexclassId(String indexclassId) {
        this.indexclassId = indexclassId;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ItemIndexclassPK)) {
            return false;
        }
        ItemIndexclassPK castOther = (ItemIndexclassPK) other;
        return
                this.itemId.equals(castOther.itemId)
                        && this.indexclassId.equals(castOther.indexclassId);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.itemId.hashCode();
        hash = hash * prime + this.indexclassId.hashCode();

        return hash;
    }

    @Override
    public String toString() {
        return "ItemIndexclassPK{" +
                "itemId='" + itemId + '\'' +
                ", indexclassId='" + indexclassId + '\'' +
                '}';
    }
}