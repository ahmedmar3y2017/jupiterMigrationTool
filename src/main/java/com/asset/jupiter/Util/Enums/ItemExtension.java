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
public enum ItemExtension {
    FOLDER("2"),
    TIF("tif"),
    PDF("pdf"),
    PNG("png"),
    JPG("jpg");

    private final String itemExtension;

    ItemExtension(String itemExtension) {
        this.itemExtension = itemExtension;
    }

    public String getItemExtension() {
        return this.itemExtension;
    }
}
