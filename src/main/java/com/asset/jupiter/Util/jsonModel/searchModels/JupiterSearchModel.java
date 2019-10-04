/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.jsonModel.searchModels;

import java.util.LinkedHashMap;

/**
 *
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
public class JupiterSearchModel {

    private KnowledgePool knowledgePool;
    private LinkedHashMap<Integer, FolderPath> foldersPath;

    public JupiterSearchModel() {
        foldersPath = new LinkedHashMap<Integer, FolderPath>();
    }

    public void addFolder(FolderPath folder) {
        int newfolder = foldersPath.size() + 1;
        foldersPath.put(newfolder, folder);
    }

    /**
     * @param foldersPath the foldersPath to set
     */
    public void setFoldersPath(LinkedHashMap<Integer, FolderPath> foldersPath) {
        this.foldersPath = foldersPath;
    }

    public LinkedHashMap<Integer, FolderPath> getFoldersPath() {
        return foldersPath;
    }

    public KnowledgePool getKnowledgePool() {
        return knowledgePool;
    }

    public void setKnowledgePool(KnowledgePool knowledgePool) {
        this.knowledgePool = knowledgePool;
    }

    @Override
    public String toString() {
        return "JupiterSearchModel{" +
                "knowledgePool=" + knowledgePool +
                ", foldersPath=" + foldersPath +
                '}';
    }
}
