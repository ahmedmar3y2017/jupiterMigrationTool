/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.Enums;

/**
 *
 * @author abdelhalim.radwan
 */
public enum ItemType {
    FOLDER("2"),
    DOCUMENT("3");

    private final String itemType;

    ItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemType() {
        return this.itemType;
    }
}
