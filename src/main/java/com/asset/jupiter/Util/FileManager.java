/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author abdelhalim.radwan
 */
public class FileManager {

    public byte[] readBytesFromFile(File file) {
        FileInputStream fileInputStream = null;
        byte[] fileBytes = null;
        try {
            if (file.exists()) {
                fileBytes = new byte[(int) file.length()];
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(fileBytes);
            }
        } catch (IOException ioexp) {
            ioexp.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ioexp) {
                    ioexp.printStackTrace();
                }
            }
        }
        return fileBytes;
    }
}
