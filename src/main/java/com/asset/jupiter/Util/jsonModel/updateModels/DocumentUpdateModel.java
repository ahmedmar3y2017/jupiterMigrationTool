/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.jsonModel.updateModels;

/**
 *
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
public class DocumentUpdateModel extends UpdateModel {
    public DocumentUpdateModel(){
        super();
        
    }
    public String path;

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "DocumentUpdateModel{" +
                "path='" + path + '\'' +
                ", indexclass='" + indexclass + '\'' +
                ", oldMetadata=" + oldMetadata +
                ", newMetadata=" + newMetadata +
                '}';
    }
}
