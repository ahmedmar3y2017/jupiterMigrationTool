package com.asset.jupiter.Util.jsonModel;

import com.asset.jupiter.Util.jsonModel.createModels.ItemModel;
import com.google.gson.Gson;

import java.util.LinkedHashMap;

/**
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
public class JsonCreateModel {
    private LinkedHashMap<Integer, ItemModel> items;
    private String documentsPhysicalPath;
    private String mode;

    public JsonCreateModel() {
        items = new LinkedHashMap<Integer, ItemModel>();
    }

    public void addItemModel(ItemModel itemModel) {
        int newfolder = items.size() + 1;
        items.put(newfolder, itemModel);
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
     * @param items the items to set
     */
    public void setItems(LinkedHashMap<Integer, ItemModel> items) {
        this.items = items;
    }

    public LinkedHashMap<Integer, ItemModel> getItems() {
        return items;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);

    }
}
