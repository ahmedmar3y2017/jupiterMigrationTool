package com.asset.jupiter.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ThreadLogs {


    public static List<String> getAllThreadLogs(String infoFileName, String folderPath) {

        List<String> allFiles = new ArrayList<>();

        File folder = new File(folderPath);
        List<File> listOfFiles = Arrays.stream(folder.listFiles()).
                filter(e -> getFileExtension(e).equals(".log")).collect(Collectors.toList());

        for (int i = 0; i < listOfFiles.size(); i++) {
            File file = listOfFiles.get(i);
            // check if is file , contain fileName , extension is .log
            if (file.isFile() &&
                    file.getName().contains(infoFileName+"_")) {

                allFiles.add(file.getPath());

            }
        }

        return allFiles;

    }

    private static String getFileExtension(File file) {
        String extension = "";

        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            extension = "";
        }

        return extension;

    }

}
