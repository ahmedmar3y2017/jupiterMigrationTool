/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.jsonModel.searchModels;


import com.asset.jupiter.Util.exceptions.JupiterException;

/**
 *
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
public class KnowledgePool {

    private String Name;
    
    /**
     * @return the Name
     */
    public String getName() {
        return Name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(String Name) throws JupiterException {
        if(isNullOrEmpty(Name)){
           throw new JupiterException("KnowledgePool Name is Null or empty", JupiterException.NULL_OR_EMPTY_VLAUE);
        }
        this.Name = Name;
    }
    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "KnowledgePool{" +
                "Name='" + Name + '\'' +
                '}';
    }
}
