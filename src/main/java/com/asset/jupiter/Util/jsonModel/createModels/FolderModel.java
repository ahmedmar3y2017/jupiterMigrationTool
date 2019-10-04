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
public class FolderModel {

    private String indexclass;
    private HashMap<String, String> metadata;
    private LinkedHashMap<Integer, DocumentModel> documents;

    public FolderModel() {
        documents = new LinkedHashMap<Integer, DocumentModel>();
    }

    public void addDocument(DocumentModel document) {
        int newfolder = getDocuments().size() + 1;
        getDocuments().put(newfolder, document);
    }

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
    public void setMetadata(HashMap<String, String> metadata) {
        this.metadata = metadata;
    }

    /**
     * @return the documents
     */
    public LinkedHashMap<Integer, DocumentModel> getDocuments() {
        return documents;
    }

    /**
     * @param documents the documents to set
     */
    public void setDocuments(LinkedHashMap<Integer, DocumentModel> documents) {
        this.documents = documents;
    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FolderModel{" +
                "indexclass='" + indexclass + '\'' +
                ", metadata=" + metadata +
                ", documents=" + documents +
                '}';
    }
}
