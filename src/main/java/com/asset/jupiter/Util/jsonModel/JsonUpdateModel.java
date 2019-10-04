/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.jsonModel;


import com.asset.jupiter.Util.jsonModel.updateModels.ItemUpdateModel;

import java.util.LinkedHashMap;

/**
 *
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
public class JsonUpdateModel {
    private String documentsPhysicalPath;
    private String mode;
    private LinkedHashMap<Integer, ItemUpdateModel>modificationItems;
    public JsonUpdateModel(){
        modificationItems = new LinkedHashMap<Integer, ItemUpdateModel>();
    }
    public void addItemUpdateModel(ItemUpdateModel itemUpdateModel){
        int newfolder = modificationItems.size() + 1;
        modificationItems.put(newfolder, itemUpdateModel);
    }
    /**
     * @return the documentsPhysicalPath
     */
    public String getDocumentsPhysicalPath() {
        return documentsPhysicalPath;
    }

    /**
     * @param documentsPhysicalPath the documentsPhysicalPath to set
     */
    public void setDocumentsPhysicalPath(String documentsPhysicalPath) {
        this.documentsPhysicalPath = documentsPhysicalPath;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the modificationItems
     */
    public LinkedHashMap<Integer, ItemUpdateModel> getModificationItems() {
        return modificationItems;
    }

    /**
     * @param modificationItems the modificationItems to set
     */
    public void setModificationItems(LinkedHashMap<Integer, ItemUpdateModel> modificationItems) {
        this.modificationItems = modificationItems;
    }

    @Override
    public String toString() {
        return "JsonUpdateModel{" +
                "documentsPhysicalPath='" + documentsPhysicalPath + '\'' +
                ", mode='" + mode + '\'' +
                ", modificationItems=" + modificationItems +
                '}';
    }
}
