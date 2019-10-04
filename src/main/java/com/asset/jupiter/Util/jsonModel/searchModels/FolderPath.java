/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.jsonModel.searchModels;


import com.asset.jupiter.Util.exceptions.JupiterException;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
public class FolderPath {
    private String indexclass;
    private LinkedHashMap<String, String> metadata;
    private String label;

    /**
     * @return the indexclass
     */
    public String getIndexclass() {
        return indexclass;
    }

    /**
     * @param indexclass the indexclass to set
     */
    public void setIndexclass(String indexclass) throws JupiterException {
        if (isNullOrEmpty(indexclass)) {
            throw new JupiterException("Indexclass Name is Null or empty", JupiterException.NULL_OR_EMPTY_VLAUE);
        }
        this.indexclass = indexclass;
    }

    /**
     * @return the metadata
     */
    public HashMap<String, String> getMetadata() {
        return metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(LinkedHashMap<String, String> metadata) {
        this.metadata = metadata;
    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.trim().isEmpty())
            return false;
        return true;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "FolderPath{" +
                "indexclass='" + indexclass + '\'' +
                ", metadata=" + metadata +
                ", label='" + label + '\'' +
                '}';
    }
}
