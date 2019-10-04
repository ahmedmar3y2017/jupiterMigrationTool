/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.jsonModel.createModels;


import com.asset.jupiter.Util.exceptions.JupiterException;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
public class DocumentModel {
    private String indexclass;
    private String path;
    private LinkedHashMap<String, String> metadata;

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
        if(isNullOrEmpty(indexclass)){
           throw new JupiterException("Indexclass Name is Null or empty", JupiterException.NULL_OR_EMPTY_VLAUE);
        }
        this.indexclass = indexclass;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) throws JupiterException {
        if(isNullOrEmpty(path)){
           throw new JupiterException("Indexclass Name is Null or empty", JupiterException.NULL_OR_EMPTY_VLAUE);
        }
        this.path = path;
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

    /**
     * check the string is null or empty
     * @param str
     * @return true if null, false if not null
     */
    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DocumentModel{" +
                "indexclass='" + indexclass + '\'' +
                ", path='" + path + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
