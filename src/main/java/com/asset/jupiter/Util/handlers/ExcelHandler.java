package com.asset.jupiter.Util.handlers;

import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Util.excelModel.ExcelModel;
import com.asset.jupiter.Util.excelModel.RowModel;
import com.asset.jupiter.Util.exceptions.PropertiesNotFoundException;
import com.asset.jupiter.Util.logging.Log;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
@Scope(value = "prototype", proxyMode = ScopedProxyMode.NO)
@Component
public class ExcelHandler {

    private static String[] columns = {"Path", "Status", "Processing Date", "Finish Status", "Desc"};
    private HashMap<Integer, String> columns_map = new HashMap<Integer, String>(5);
    public static String infoFilePath = null;
    // excel File
    File excelFile;
    // excel Model
    ExcelModel excelModel;

    public ExcelHandler() throws PropertiesNotFoundException, IOException {

//        String pathLogXml = new ClassPathResource("src/main/resources/log4j.xml").getPath();
//        String pathFolderLog = new ClassPathResource("src/main/resources/log").getPath();
//        Log.initialize(pathFolderLog, pathLogXml);
        Properties props = new Properties();

        props.load(getClass().getClassLoader().getResourceAsStream("application.properties"));

        if (props.contains("automatic_Mode") &&
                !props.getProperty("automatic_Mode").equalsIgnoreCase(Defines.auto)) {
            if (props.containsKey(Defines.infofilepath)) {
                Log.info(ExcelHandler.class.getName(), "Excel Info File Path: ");
                infoFilePath = props.getProperty(Defines.infofilepath) + "\\" + Defines.infoFile;
                excelFile = new File(infoFilePath);
                if (!isInfoFileCreated()) {
                    throw new FileNotFoundException("Excel Info File is not exist.");
                }
            } else {
                Log.error(ExcelHandler.class.getName(), "Defines.infofilepath property is missing in application.properties file");
                throw new PropertiesNotFoundException("Defines.infofilepath property is missing in config.properties file", PropertiesNotFoundException.MISSING_PROPERTY);
            }

        }


    }

    /**
     * this method is to create excel info file if not exist
     *
     * @throws IOException
     */
    public void createInfoFile() throws IOException {
        FileOutputStream fileOut = null;
        try {
            for (int i = 0; i < columns.length; i++) {
                String column = columns[i];
                columns_map.put(i + 1, column);
            }
            // Create a Workbook
            //Workbook workbook = new XSSFWorkbook();     // new HSSFWorkbook() for generating `.xls` file
            XSSFWorkbook workbook = new XSSFWorkbook();
            /* CreationHelper helps us create instances for various things like DataFormat,
            Hyperlink, RichTextString etc in a format (HSSF, XSSF) independent way */
            CreationHelper createHelper = workbook.getCreationHelper();
            // Create a Sheet
            //Sheet sheet = workbook.createSheet("FilesInfo");
            XSSFSheet sheet = workbook.createSheet("FilesInfo");
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
            XSSFDataValidationConstraint StatusConstraint = (XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint(new String[]{"Ready", "InProgress", "Finished"});
            XSSFDataValidationConstraint CompleteConstraint = (XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint(new String[]{"Success", "Contain Errors"});
            CellRangeAddressList fileStatusList = new CellRangeAddressList(1, 100, 1, 1);
            CellRangeAddressList finishStatusList = new CellRangeAddressList(1, 100, 3, 3);
            XSSFDataValidation Statusvalidation = (XSSFDataValidation) dvHelper.createValidation(StatusConstraint, fileStatusList);
            XSSFDataValidation Finishvalidation = (XSSFDataValidation) dvHelper.createValidation(CompleteConstraint, finishStatusList);
            Statusvalidation.setSuppressDropDownArrow(true);
            Statusvalidation.setShowErrorBox(true);
            Statusvalidation.setShowPromptBox(true);
            Statusvalidation.setEmptyCellAllowed(false);
            Finishvalidation.setSuppressDropDownArrow(true);
            Finishvalidation.setShowErrorBox(true);
            Finishvalidation.setShowPromptBox(true);
            Finishvalidation.setEmptyCellAllowed(false);
            // Create a Font for styling header cells
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.RED.getIndex());
            // Create a CellStyle with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            headerCellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
            // Create a Row
            Row headerRow = sheet.createRow(0);
            // Creating cells
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }
            for (Map.Entry<Integer, String> column : columns_map.entrySet()) {
                Integer key = column.getKey();
                String value = column.getValue();
                Log.info(ExcelHandler.class.getName(), "Key: " + key + " ,Value: " + value);
                if (key == 3) {
                }
            }
            sheet.addValidationData(Statusvalidation);
            sheet.addValidationData(Finishvalidation);
//            Cell optionCell = headerRow.createCell(columns.length);
//            optionCell.setCell(Statusvalidation);
            // Cell Style for formatting Date
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));
            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            // Write the output to a file
            fileOut = new FileOutputStream(infoFilePath);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (FileNotFoundException ex) {
            Log.error("File Not Found Exception: Excel file is not exist in the path ", ex);
            throw ex;
        } catch (IOException ex) {
            Log.error("IOException happen while creating excel Info file", ex);
            throw ex;
        } finally {
            try {
                fileOut.close();
            } catch (IOException ex) {
                Log.error("IOException happen while closing the excel file", ex);
                throw ex;
            }
        }
    }

    /**
     * this method is to read excel info file
     */
    public void readInfoFile() {

    }

    /**
     * this method is to get ready paths in excel info file
     *
     * @return SortedArrayList
     */
    public ExcelModel getReadyPathes() throws IOException {

//        SortedArrayList<String> readyPaths = new SortedArrayList<String>();
        String path = null;
        String status = null;
        if (isInfoFileCreated()) {
//            File excelFile = new File(infoFilePath);
            FileInputStream fileInputStream;
            fileInputStream = new FileInputStream(excelFile);
            ZipSecureFile.setMinInflateRatio(-1.0d);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            // excel Model
            excelModel = new ExcelModel();

            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                if (i == 0) {
                    continue;
                }
                Row row = sheet.getRow(i);

                Cell pathCell = row.getCell(0);
                Cell statusCell = row.getCell(1);
                Cell processingDateCell = row.getCell(2);
                Cell finishstatusCell = row.getCell(3);
                Cell descCell = row.getCell(4);
                path = pathCell.getStringCellValue();
                status = statusCell.getStringCellValue();
                if (status.equals("Ready")) {
//                    row.getRowNum();
                    // create row model
                    RowModel rowModel = new RowModel();
                    rowModel.setRowNum(row.getRowNum());
                    rowModel.setPath(path);
                    rowModel.setStatus(status);
//                    RowModel.setFinishStatus(finishstatusCell.getStringCellValue());
//                    RowModel.setProcessingDate(processingDateCell.getStringCellValue());
//                    RowModel.setDesc(descCell.getStringCellValue());

                    // put it into  create excel model
                    excelModel.getrowlModels().add(rowModel);

//                    readyPaths.insertSorted(path);
                }

            }
            // close file input stream

        } else {
            throw new FileNotFoundException("Excel Info File is not exist.");
        }
        return excelModel;
    }

    /**
     * this method is to get in progress files paths from excel info file
     *
     * @return SortedArrayList
     */
    public SortedArrayList getInProgressPathes() throws IOException {
        SortedArrayList<String> inProgressPaths = new SortedArrayList<String>();
        String path = null;
        String status = null;
        if (isInfoFileCreated()) {
//            File excelFile = new File(infoFilePath);
            FileInputStream fileInputStream;
            fileInputStream = new FileInputStream(excelFile);
            ZipSecureFile.setMinInflateRatio(-1.0d);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                if (i == 0) {
                    continue;
                }
                Row row = sheet.getRow(i);
                Cell pathCell = row.getCell(0);
                Cell statusCell = row.getCell(1);
                path = pathCell.getStringCellValue();
                status = statusCell.getStringCellValue();
                if (status.equals("InProgress")) {
                    inProgressPaths.insertSorted(path);
                }

            }
        } else {
            throw new FileNotFoundException("Excel Info File is not exist.");
        }
        return inProgressPaths;
    }

    /**
     * this method is to get finished files paths from excel info file
     *
     * @return SortedArrayList
     */
    public SortedArrayList getFinishedPathes() {
        SortedArrayList<String> finishedPaths = new SortedArrayList<String>();
        return finishedPaths;
    }

    /**
     * this method is to check if info file is exist or not
     *
     * @return
     */
    public boolean isInfoFileCreated() {
//        File excelInfoFile = new File(infoFilePath);
        return excelFile.exists();
    }

    class SortedArrayList<T> extends ArrayList<T> {

        @SuppressWarnings("unchecked")
        public void insertSorted(T value) {
            add(value);
            Comparable<T> cmp = (Comparable<T>) value;
            for (int i = size() - 1; i > 0 && cmp.compareTo(get(i - 1)) < 0; i--) {
                Collections.swap(this, i, i - 1);
            }
        }
    }

    // if start Thread
    public synchronized void updateInfoFileOnStartThread(int row) throws IOException {
        try {

            FileInputStream file = new FileInputStream(infoFilePath);
            ZipSecureFile.setMinInflateRatio(-1.0d);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Cell cell = null;

            //Update the value of status cell
            cell = sheet.getRow(row).getCell(1);
            cell.setCellValue("InProgress");

            //Update the value of processing date
            cell = sheet.getRow(row).getCell(2);
            cell.setCellValue(Calendar.getInstance().getTime());
            file.close();

            FileOutputStream outFile = new FileOutputStream(excelFile);
            workbook.write(outFile);
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // if thread finish but Success All
    public synchronized void updateInfoFileOnEndSuccessThread(int row) throws IOException {
        try {

            FileInputStream file = new FileInputStream(infoFilePath);
            ZipSecureFile.setMinInflateRatio(-1.0d);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Cell cell = null;

            //Update the value of status cell
            cell = sheet.getRow(row).getCell(1);
            cell.setCellValue("Finished");

            //Update the value of processing date
            cell = sheet.getRow(row).getCell(2);
            cell.setCellValue("Success");
            file.close();

            FileOutputStream outFile = new FileOutputStream(excelFile);
            workbook.write(outFile);
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // if thread finish but contain errors
    public synchronized void updateInfoFileOnEndFaildThread(int row) throws IOException {
        try {

            FileInputStream file = new FileInputStream(infoFilePath);
            ZipSecureFile.setMinInflateRatio(-1.0d);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Cell cell = null;

            //Update the value of status cell
            cell = sheet.getRow(row).getCell(1);
            cell.setCellValue("Finished");

            //Update the value of processing date
            cell = sheet.getRow(row).getCell(2);
            cell.setCellValue("Contain Errors");
            file.close();

            FileOutputStream outFile = new FileOutputStream(excelFile);
            workbook.write(outFile);
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
