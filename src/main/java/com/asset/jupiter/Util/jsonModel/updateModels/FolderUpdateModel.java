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
public class FolderUpdateModel extends UpdateModel {
    public FolderUpdateModel(){
        super();
    }

    @Override
    public String toString() {
        return "FolderUpdateModel{" +
                "indexclass='" + indexclass + '\'' +
                ", oldMetadata=" + oldMetadata +
                ", newMetadata=" + newMetadata +
                '}';
    }
}
