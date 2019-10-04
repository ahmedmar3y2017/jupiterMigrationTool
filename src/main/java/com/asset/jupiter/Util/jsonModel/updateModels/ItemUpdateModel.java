/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.jsonModel.updateModels;

import com.asset.jupiter.Util.jsonModel.searchModels.JupiterSearchModel;

import java.util.LinkedHashMap;

/**
 *
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
public class ItemUpdateModel {

    private JupiterSearchModel jupiterPath;
    private LinkedHashMap<Integer, FolderUpdateModel> folders;
    private LinkedHashMap<Integer, DocumentUpdateModel> documents;

    public ItemUpdateModel() {
        folders = new LinkedHashMap<Integer, FolderUpdateModel>();
        documents = new LinkedHashMap<Integer, DocumentUpdateModel>();
    }

    public void addDocumentUpdateModel(DocumentUpdateModel documentUpdateModel) {
        int newDocument = documents.size() + 1;
        documents.put(newDocument, documentUpdateModel);
    }

    public void addFolderUpdateModel(FolderUpdateModel folderUpdateModel) {
        int newFolder = folders.size()+1;
        folders.put(newFolder, folderUpdateModel);
    }

    /**
     * @return the jupiterPath
     */
    public JupiterSearchModel getJupiterPath() {
        return jupiterPath;
    }

    /**
     * @param jupiterPath the jupiterPath to set
     */
    public void setJupiterPath(JupiterSearchModel jupiterPath) {
        this.jupiterPath = jupiterPath;
    }

    /**
     * @return the folders
     */
    public LinkedHashMap<Integer, FolderUpdateModel> getFolders() {
        return folders;
    }

    /**
     * @param folders the folders to set
     */
    public void setFolders(LinkedHashMap<Integer, FolderUpdateModel> folders) {
        this.folders = folders;
    }

    /**
     * @return the documents
     */
    public LinkedHashMap<Integer, DocumentUpdateModel> getDocuments() {
        return documents;
    }

    /**
     * @param documents the documents to set
     */
    public void setDocuments(LinkedHashMap<Integer, DocumentUpdateModel> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "ItemUpdateModel{" +
                "jupiterPath=" + jupiterPath +
                ", folders=" + folders +
                ", documents=" + documents +
                '}';
    }
}
