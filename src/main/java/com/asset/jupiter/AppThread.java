package com.asset.jupiter;

import com.asset.jupiter.JUPITER.Model.Entities.InfoFile;
import com.asset.jupiter.Util.MailConfig.SendMail;
import com.asset.jupiter.Util.MailConfig.mail;
import com.asset.jupiter.Util.ThreadLogs;
import com.asset.jupiter.Util.configurationService.config;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.handlers.ExcelHandler;
import com.asset.jupiter.Util.handlers.JsonHandler;
import com.asset.jupiter.Util.jsonModel.JsonCreateModel;
import com.asset.jupiter.Util.jsonModel.JsonUpdateModel;
import com.asset.jupiter.Util.logging.ThreadLogger;
import com.asset.jupiter.Util.operations.DocumentHandler;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.apache.commons.io.FilenameUtils;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.NO)
public class AppThread {

    @Autowired
    JsonHandler jsonHandler;
    @Autowired
    DocumentHandler documentHandler;
    @Autowired
    ExcelHandler excelHandler;
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.infoFileService infoFileService;
    // configuration Check
    @Autowired
    com.asset.jupiter.Util.configurationService.config config;
    @Autowired
    SendMail sendMail;

    public void start(String type,
                      String path,
                      int rowCount,
                      InfoFile infoFile,
                      String threadId)throws JupiterException {
        long startJson = System.currentTimeMillis();
        Path p = Paths.get(path);
        String jsonName = p.getFileName().toString();
        String fileNameWithOutExt = FilenameUtils.removeExtension(jsonName);
        ThreadLogger log = new ThreadLogger(threadId, fileNameWithOutExt);
		log.info(threadId, "Thead " + threadId + " stat for JSON " + path);
        // create Folder in shared Folder if Not EXists
        String integrationFolderPath = config.getIntegrationFolderPath();

        // create Finished Folder
        File file = new File(integrationFolderPath + "\\Finished");
        if (!file.exists()) {
            file.mkdirs();

        }
        // create Error Folders
        File fileError = new File(integrationFolderPath + "\\Contain_Errors");
        if (!fileError.exists()) {
            fileError.mkdirs();

        }
        // create Failed Folders
        File fileFailed = new File(integrationFolderPath + "\\Failed");
        if (!fileFailed.exists()) {
            fileFailed.mkdirs();

        }
        // begin thread for json handler
        String jsonPath = path;
        Object objectJson = null;
        try {
            objectJson = jsonHandler.readJson(jsonPath, threadId, fileNameWithOutExt);


            //Log.info(threadId, "Finish Read Json Model ");
            log.info(threadId, "finish read JSON model");
            // update Excel File to in processing
            //  update Excel status , processing date   ;
            if (type.equalsIgnoreCase(Defines.excel)) {
                try {
                    //Log.info(threadId, "Update Excel Row That started");
                    log.info(threadId, "Update Excel Row That started");
                    excelHandler.updateInfoFileOnStartThread(rowCount);
                } catch (IOException e) {
                    //Log.error(threadId, "Error when updateInfoFileOnStartThread in excel  ");
                    log.error(threadId, "error when updateing InfoFile on start Thread in excel mode.", e);
                    throw new JupiterException("Error when updateInfoFileOnStartThread in excel " + e.getMessage(), JupiterException.IO_EXCEPTION);
                }
            } else {//mode is automatic
                // if Object Json == null ---- >  json failed
                if (objectJson == null ){
                    // there are error on parsing
                    log.debug(threadId, "Error In Json File - > " + infoFile.getPath());
                    // update database For this Path in progress
                    infoFile.setStatus(Defines.finished);
                    infoFile.setFinishStatus(Defines.containErrors);
                    // set current Time
                    infoFile.setFinishDate(new Timestamp(new Date().getTime()));

                    infoFile.setProcessingDate(new Timestamp(new Date().getTime()));
                    // update into database
                    updateDBStatus(infoFile);
                    // move file to failed Folder inside integration Folder
                    // move this File to Failed Folder
                    Path temp = null;
                    try {
                        // integration Path
//                        String integrationFolderPath = config.getIntegrationFolderPath();
                        String newFile = integrationFolderPath + "\\Failed\\" + path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf(".")) + "-FAILED.json";
                        temp = Files.copy
                                (Paths.get(path),
                                        Paths.get(newFile), StandardCopyOption.REPLACE_EXISTING);
//                  delete This File
                        File file1 = new File(path);
                        if (file1.exists())
                            Files.deleteIfExists(Paths.get(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (temp != null) {
                    } else {
                    }


                    // send mail with log file
                    try {
                        log.debug(threadId, "failed on json File - > " + infoFile.getPath());
                        String infoFileName= infoFile.getPath().substring(infoFile.getPath().lastIndexOf("\\") + 1, infoFile.getPath().lastIndexOf("."));
                        // ThreadLogs.getAllThreadLogs(infoFileName ,config.getTool_resources()  + "/log" );
                        sendMail.prepareAndSendTemplate(new mail("", "",  infoFileName+"  - >  document physical file path Not correct " ,
                                        "There are Exceptions that Occured During processing the following json File: "
                                                + infoFile.getPath()
                                                + " kindly Check Jupiter Tool Log File for details Time : "
                                                + new Date()),
                                Defines.errorImage  , true , infoFile.getPath()
                        );

                    } catch (MessagingException e) {
                        log.error(threadId, "error in send mail to admin user", e);
                        e.printStackTrace();
                    }
                }else
                {
                    // update database For this Path in progress
                    infoFile.setProcessingDate(new Timestamp(new Date().getTime()));
                    infoFile.setStatus("InProcessing");
                    // update Database
                    log.debug(threadId, "update database INFOFILE from ready to inprocessing");
                    updateDBStatus(infoFile);
                }


            }
        } catch (JupiterException e) {
            log.error(threadId, "Error readJson : " + jsonPath, e);
            log.LogFunctionExecution(threadId, "AppThread.start", System.currentTimeMillis() - startJson, e);
            throw new JupiterException("Error readJson : " + jsonPath + e.getMessage());
        }
        String jsonMode = jsonHandler.getMode();
        //Log.debug(threadId, "Json Mode is :: " + jsonMode);
        // check documentsPhysicalPath
        // check Mode
        switch (jsonMode) {
            case Defines.createMode:
                // create Mode
                //Log.debug(threadId, "getJsonCreateModel " + Defines.createMode);
                log.debug(threadId, "JSON mode: " + Defines.createMode);
                JsonCreateModel jsonCreateFileModel = (JsonCreateModel) objectJson;
                String documentsPhysicalPath = jsonCreateFileModel.getDocumentsPhysicalPath();
                // call check Path
                log.debug(threadId, "check Docuements Physical path is exits or not.");
                boolean checkPath = checkDocumentsPhysicalPath(documentsPhysicalPath);
                // if error In Path
                if (!checkPath) {
                    // Change JSON file in DB
                    // update database that finish
                    infoFile.setStatus(Defines.finished);
                    infoFile.setFinishStatus(Defines.containErrors);
                    // set current Time
                    infoFile.setFinishDate(new Timestamp(new Date().getTime()));

                    // update Database
                    updateDBStatus(infoFile);
                    // move file to failed Folder inside integration Folder
                    // move this File to Failed Folder
                    Path temp = null;
                    try {
                        // integration Path
//                        String integrationFolderPath = config.getIntegrationFolderPath();
                        String newFile = integrationFolderPath + "\\Failed\\" + path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf(".")) + "-FAILED.json";
                        temp = Files.copy
                                (Paths.get(path),
                                        Paths.get(newFile), StandardCopyOption.REPLACE_EXISTING);
//                  delete This File
                        File file1 = new File(path);
                        if (file1.exists())
                            Files.deleteIfExists(Paths.get(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (temp != null) {
                    } else {
                    }

                    // log
//                    Log.info(threadId, "   Error In checkDocumentsPhysicalPath " + documentsPhysicalPath);
//                    Log.error(threadId, "   Error In checkDocumentsPhysicalPath " + documentsPhysicalPath);
                    log.error(threadId, "error: documents physical path is not exist or user has no read/write access");
                    //  Send Mail
                    try {
						log.debug(threadId, "send mail to admin user for documents physical path");
                       String infoFileName= infoFile.getPath().substring(infoFile.getPath().lastIndexOf("\\") + 1, infoFile.getPath().lastIndexOf("."));
                      // ThreadLogs.getAllThreadLogs(infoFileName ,config.getTool_resources()  + "/log" );
                        sendMail.prepareAndSendTemplate(new mail("", "",  infoFileName+"  - >  document physical file path Not correct " ,
                                "There are Exceptions that Occured During processing the following json File: "
                                + infoFile.getPath()
                                + "  document physical file path Not correct "
                                + " kindly Check Jupiter Tool Log File for details Time : "
                                + new Date()),
                                Defines.errorImage  , true , infoFile.getPath()
                        );

                    } catch (MessagingException e) {
                        log.error(threadId, "error in send mail to admin user", e);
                        e.printStackTrace();
                    }
                    //throw Exception
                    try {
                        throw new JupiterException(threadId + "   Error In checkDocumentsPhysicalPath " + documentsPhysicalPath);
                    } catch (JupiterException e) {
                        log.LogFunctionExecution(threadId, "AppThread.start", System.currentTimeMillis() - startJson, e);
                        e.printStackTrace();
                    }
                    log.debug(threadId, "end thread: " + threadId);
                    //End Thread
//                    Thread.currentThread().interrupt();
//                    Thread.currentThread().stop();
                    return;
                }
                log.debug(threadId, "documents physical path is exist and user can read/write");
                try {
                    //Log.info(threadId, "Begin create Processes ");
                    long createTime = System.currentTimeMillis();
                    log.debug(threadId, "creation process in Jupiter DB begin...");
                    documentHandler.jupiterCreateModel(jsonCreateFileModel, infoFile, threadId, path,log);
                    switch (type) {
                        case Defines.excel:
                            // update excel finish by rowCount
                            break;
                        case Defines.auto:
//                            // update database that finish
//                            infoFile.setStatus("Finished");
//                            infoFile.setFinishStatus("Success");
//                            // set current Time
//                            infoFile.setFinishDate(new Timestamp(new Date().getTime()));
//                            // update Database
//                            updateDBStatus(infoFile);
                            break;
                        default:
                            break;
                    }
                    // update
                    long endTime = System.currentTimeMillis()-createTime;
                    log.info(threadId, "End create Processes and ecuted in "+endTime+" milliseconds");
                } catch (ExecutionException e) {
                    log.error(threadId, "ExecutionException while creation processing", e);
                    log.LogFunctionExecution(threadId, "AppThread.start",System.currentTimeMillis()-startJson,e);
                    throw new JupiterException("Execution Create Items Exception Execution Create Items Exception ExecutionException" + e.getMessage(), JupiterException.Execution_Exception);
                } catch (InterruptedException e) {
                    log.error(threadId, "InterruptedException while creation processing", e);
                    log.LogFunctionExecution(threadId, "AppThread.start",System.currentTimeMillis()-startJson,e);
                    throw new JupiterException("Execution Create Items Exception InterruptedException" + e.getMessage(), JupiterException.Interrupted_Exception);
                }
                break;
            case Defines.updateMode:
                // update Mode
                JsonUpdateModel jsonUpdateFileModel = (JsonUpdateModel) objectJson;
                log.info(threadId, "get json update folder Model : ");
                log.info(threadId, "Begin update Processes ");
                documentHandler.updateDocument(jsonUpdateFileModel);
                log.info(threadId, "End update Processes ");

                break;
            default:
                log.error(threadId, "Json No Mode Matched (create , update)");
                break;
        }
//        // checking free memory
//        System.out.println("before : "
//                + gfg.freeMemory());
//        // calling the garbage collector on demand
//        Log.info("Begin Garbage Collection ");
//        // after each Thread call Garbage Collection
//        gfg.gc();
//        Log.info("After Garbage Collection ");
//        System.out.println("Free memory after garbage "
//                + "collection: " + gfg.freeMemory());
    }
    /**
     * If path is not existing or not directory and can access read and write
     *
     * @param path
     * @return
     */
    public boolean checkDocumentsPhysicalPath(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory() && file.canRead() && file.canWrite()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Update Json File database status
     *
     * @param infoFile
     */
    public void updateDBStatus(InfoFile infoFile) {
        // update Database
        infoFileService.updateInfoFile(infoFile);
    }
}
