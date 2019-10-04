package com.asset.jupiter.Util;

import com.asset.jupiter.Util.Enums.JupiterMediaExtension;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author abdelhalim.radwan
 */
public class JupFileBuilder {

    public boolean createFiles(String physicalPath, byte[] data, String locationID, String originalFileExtension) {


        createJupFile(physicalPath, "AssetPE02012003".getBytes(), data, locationID,
                JupiterMediaExtension.JUP, originalFileExtension);
//        createThumbnailFile(physicalPath, "AssetPE02012003".getBytes(), data, locationID,
//                JupiterMediaExtension.THUMB, originalFileExtension);
        return true;
    }

    private boolean createJupFile(String physicalPath, byte[] signature, byte[] data, String locationID,
                                  JupiterMediaExtension extension, String originalFileExtension) {
        return createFile(physicalPath, signature, data, locationID, extension, originalFileExtension);
    }

    private boolean createThumbnailFile(String physicalPath, byte[] signature, byte[] data, String locationID,
                                        JupiterMediaExtension extension, String originalFileExtension) {
        return createFile(physicalPath, signature, data, locationID, extension, originalFileExtension);
    }

    private boolean createFile(String physicalPath, byte[] signature, byte[] data, String locationID,
                               JupiterMediaExtension extension, String originalFileExtension) {
        if (data != null && data.length != 0 && locationID != null && locationID.length() != 0) {
            String filePath = getFullPath(locationID, extension);
            filePath = physicalPath + filePath;
            // create Folders And Sub Folders If Not Exsists
            File f = new File(filePath);

            try {
                Files.createDirectories(Paths.get(f.getParent()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                File file = new File(filePath);

                if (!file.exists()) {
                    file.createNewFile();
                }
                try (FileOutputStream outputStream = new FileOutputStream(file, true)) {
                    outputStream.write(signature);
                    if (extension.equals(JupiterMediaExtension.JUP)) {
                        outputStream.write(data);
                    } else if (extension.equals(JupiterMediaExtension.THUMB)) {
                        //byte[] thumbnailData = Thumbnailer.getThumbnail(originalFileExtension, data, 0, 180);
                        //outputStream.write(thumbnailData);
                    }
                    outputStream.close();
                }
            } catch (IOException ioexp) {
                ioexp.printStackTrace();
            }
        }
        return true;
    }

    private String getFullPath(String locationID, JupiterMediaExtension extension) {
        int parentNameLength = 0;
        StringBuilder dirPath = new StringBuilder();
        while (Character.isLetter(locationID.charAt(parentNameLength))) {
            dirPath.append(locationID.charAt(parentNameLength));
            parentNameLength++;
        }
        // taking the Idprefix as the parent directory and adding a file separator
        locationID = locationID.substring(parentNameLength);
        parentNameLength = 0;
        while (parentNameLength < locationID.length()) {
            if ((parentNameLength % 4) == 0) {
                dirPath.append(File.separator);
            }
            dirPath.append(locationID.charAt(parentNameLength));
            parentNameLength++;

        }
        if (extension.equals(JupiterMediaExtension.JUP)) {
            return dirPath.append(extension.getExtension()).toString();

        } else if (extension.equals(JupiterMediaExtension.THUMB)) {
            return dirPath.append(extension.getExtension()).toString();

        }
        return dirPath.toString();
    }
}
