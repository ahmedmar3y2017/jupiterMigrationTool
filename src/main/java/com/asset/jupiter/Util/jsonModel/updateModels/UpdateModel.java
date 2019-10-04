/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.jsonModel.updateModels;

import java.util.HashMap;

/**
 *
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
public class UpdateModel {
    public String indexclass;
    public  HashMap<String, String> oldMetadata;
    public HashMap<String, String> newMetadata;

    /**
     * @return the indexclass
     */
    public String getIndexclass() {
        return indexclass;
    }

    /**
     * @param indexclass the indexclass to set
     */
    public void setIndexclass(String indexclass) {
        this.indexclass = indexclass;
    }

    /**
     * @return the oldMetadata
     */
    public HashMap<String, String> getOldMetadata() {
        return oldMetadata;
    }

    /**
     * @param oldMetadata the oldMetadata to set
     */
    public void setOldMetadata(HashMap<String, String> oldMetadata) {
        this.oldMetadata = oldMetadata;
    }

    /**
     * @return the newMetadata
     */
    public HashMap<String, String> getNewMetadata() {
        return newMetadata;
    }

    /**
     * @param newMetadata the newMetadata to set
     */
    public void setNewMetadata(HashMap<String, String> newMetadata) {
        this.newMetadata = newMetadata;
    }

    @Override
    public String toString() {
        return "UpdateModel{" +
                "indexclass='" + indexclass + '\'' +
                ", oldMetadata=" + oldMetadata +
                ", newMetadata=" + newMetadata +
                '}';
    }
}
