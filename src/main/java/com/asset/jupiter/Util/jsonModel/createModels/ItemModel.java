/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.jsonModel.createModels;


import com.asset.jupiter.Util.jsonModel.searchModels.JupiterSearchModel;

import java.util.LinkedHashMap;

/**
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
public class ItemModel {

    private JupiterSearchModel jupiterPath;
    private LinkedHashMap<Integer, FolderModel> folders;

    public ItemModel() {
        folders = new LinkedHashMap<Integer, FolderModel>();
    }

    public void addFolder(FolderModel folder) {
        int newfolder = folders.size() + 1;
        getFolders().put(newfolder, folder);
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
    public LinkedHashMap<Integer, FolderModel> getFolders() {
        return folders;
    }

    /**
     * @param folders the folders to set
     */
    public void setFolders(LinkedHashMap<Integer, FolderModel> folders) {
        this.folders = folders;
    }

    @Override
    public String toString() {
        return "ItemModel{" +
                "jupiterPath=" + jupiterPath +
                ", folders=" + folders +
                '}';
    }
}
