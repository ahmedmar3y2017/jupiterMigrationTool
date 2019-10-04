package com.asset.jupiter.Util.operations;

import com.asset.jupiter.JUPITER.Dao.itemRepo;
import com.asset.jupiter.JUPITER.Model.Entities.*;
import com.asset.jupiter.JUPITER.jupiterService.*;
import com.asset.jupiter.JUPITERMEDIA.Model.Entities.Ftrscheduler;
import com.asset.jupiter.JUPITERMEDIA.Model.Entities.Mediamanager;
import com.asset.jupiter.JUPITERMEDIA.Model.Entities.MediamanagerPK;
import com.asset.jupiter.JUPITERMEDIA.jupiterMediaService.ftrschedulerService;
import com.asset.jupiter.JUPITERMEDIA.jupiterMediaService.mediamanagerService;
import com.asset.jupiter.JUPITERMEDIA.jupiterMediaService.rollBackJupiter4Med;
import com.asset.jupiter.Util.Enums.ItemType;
import com.asset.jupiter.Util.FileManager;
import com.asset.jupiter.Util.JupFileBuilder;
import com.asset.jupiter.Util.MailConfig.SendMail;
import com.asset.jupiter.Util.MailConfig.mail;
import com.asset.jupiter.Util.ThreadLogs;
import com.asset.jupiter.Util.configurationService.config;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.jsonModel.JsonCreateModel;
import com.asset.jupiter.Util.jsonModel.JsonUpdateModel;
import com.asset.jupiter.Util.jsonModel.createModels.DocumentModel;
import com.asset.jupiter.Util.jsonModel.createModels.FolderModel;
import com.asset.jupiter.Util.jsonModel.createModels.ItemModel;
import com.asset.jupiter.Util.jsonModel.searchModels.FolderPath;
import com.asset.jupiter.Util.jsonModel.searchModels.JupiterSearchModel;
import com.asset.jupiter.Util.logging.Log;
import com.asset.jupiter.Util.logging.ThreadLogger;
import com.asset.jupiter.Util.staticsModel.ActionListener;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Scope(value = "prototype", proxyMode = ScopedProxyMode.NO)
@Service
public class DocumentHandler {

    // accesstemplates service jpa
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.accesstemplatesService accesstemplatesService;
    // knowledge service jpa
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.knowledgepoolService knowledgepoolService;
    //indexClass Service jpa
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.indexclassService indexclassService;
    //links Service jpa
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.linksService linksService;
    //Item Service jpa
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.itemsService itemsService;
    @Autowired
    itemRepo itemRepo;
    //tables Service jpa
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.tablesService tablesService;
    //itemIndexClass Service jpa
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.itemIndexclassService itemIndexclassService;

    //   indexClass Model;
    Indexclass indexclassParent;
    // item Model
    Item itemModelParent;

    // configuration Check
    @Autowired
    config config;
    // ftrschedulerService service
    @Autowired
    ftrschedulerService ftrschedulerService;
    // mediamanagerService service
    @Autowired
    mediamanagerService mediamanagerService;
    // itemsCountService service
    @Autowired
    itemsCountService itemsCountService;
    // mediaServerService service
    @Autowired
    mediaServerService mediaServerService;
    // fieldService service
    @Autowired
    fieldService fieldService;

    // fieldService service
    @Autowired
    accessRuleService accessRuleService;

    // dynamicService service to indexClass Tables
    @Autowired
    dynamicService dynamicService;
    // digital signature
    int useDigitalSignature = 0;

    // create action Listsner For each thread (Json File)
    @Autowired
    ActionListener actionListener;

    // rollback JUPITER4
    @Autowired
    rollBackJupiter4 rollBackJupiter4;
    // rollback JUPITER4MED
    @Autowired
    rollBackJupiter4Med rollBackJupiter4Med;

    // Send Mail
    @Autowired
    SendMail sendMail;

    // file Service
    @Autowired
    jsonFileService jsonFileService;
    // actionListener Service
    @Autowired
    actionListenerService actionListenerService;
    // exception Service
    @Autowired
    exceptionService exceptionService;
    // infoFileService Service
    @Autowired
    infoFileService infoFileService;
    // viewFieldService Service
    @Autowired
    viewFieldService viewFieldService;

    String threadId = "";
    ThreadLogger log = null;

    // init to check Configuration
    @PostConstruct
    public void init() {
        // init code goes here
        // check If use Digital Signature
        Log.info(DocumentHandler.class.getName(), "Start Check If signature Work Or Not ");
        if (config.getUseDigitalSignature().equalsIgnoreCase("true")) {
            useDigitalSignature = 1;
            Log.info(DocumentHandler.class.getName(), "Digital Signature Done Work : ");
        }
//        // create Action Listener
//        actionListener = new ActionListener();
    }

    /*
     *  1 - get items
     * 2 - get itemModels
     * 3 - iterate on items to execute all folders search in jupiter db
     * 4 - execute search Method .
     * 5 - get itemId and className after search else throw exception
     *
     * */
    //    @Async("jsonAsyncExecutor")
    public void jupiterCreateModel(JsonCreateModel jsonCreateFileModel,
            InfoFile infoFile,
            String threadId,
            String path, ThreadLogger logger) throws ExecutionException, InterruptedException {
        // set thread Id Global
        this.threadId = threadId;
        this.log = logger;
        // create document Business
        long start = System.currentTimeMillis();
        String pysicalPath = jsonCreateFileModel.getDocumentsPhysicalPath();
        log.debug(this.threadId, "JSON documentsPhysicalPath  : " + pysicalPath);
        LinkedHashMap<Integer, ItemModel> items = jsonCreateFileModel.getItems();
        AtomicInteger foldersCount = new AtomicInteger();
        AtomicInteger documentsCount = new AtomicInteger();
        Set<Integer> itemNum = items.keySet();

        // number of search Methods
        itemNum.forEach(itemNumKey -> {
            // item Model
            ItemModel itemModel = items.get(itemNumKey);
            JupiterSearchModel jupiterSearchModel = itemModel.getJupiterPath();
            log.debug(this.threadId, "Item Model Path in jupiter : " + itemModel.getJupiterPath());
            // function to search Before Insert document
            String itemParentId = null;
            try {
                // search For root parent Json before anyThing
                log.debug(this.threadId, "search in jupiter to get parentId");
                itemParentId = searchModel(jupiterSearchModel, infoFile);
            } catch (JupiterException e) {
                e.printStackTrace();
                // call Method To some process if path error
                errorJupiterPath(infoFile);
                return;
            }
            log.debug(this.threadId, "End Search Functions : parent result Id: " + itemParentId);
            // check result search Method
            if (itemParentId != null && indexclassParent != null && itemModelParent != null) {
                log.info(this.threadId, "Done search Model");
                // prepare to insert into jupiter
                String primaryTableId = indexclassParent.getPrimarytableid();
                log.debug(this.threadId, "itemParentId : " + itemParentId + "\n indexclassParent : " + indexclassParent.getClassname()
                        + "\n primaryTableId : " + primaryTableId);
                // begin create folder and documents inside it
                // iterate for each Folders in this Method
                log.debug(this.threadId, "Start createFoldersAndDocuments Methods");
                // calc folders Count and documents Count
                foldersCount.set(itemModel.getFolders().size());
                documentsCount.set(itemModel.getFolders().values().stream().mapToInt(folderModel -> folderModel.getDocuments().size()).sum());
                log.debug(this.threadId, "Number of Folders in json : " + itemModel.getFolders().size());

                // start folder and document creation
                createFoldersAndDocuments(itemModel.getFolders(),
                        itemModelParent,
                        pysicalPath, infoFile, foldersCount.get(), documentsCount.get());
                log.debug(this.threadId, "Item id is: " + itemModelParent.getItemid());
                log.LogFunctionExecution(this.threadId, "jupiterCreateModel", System.currentTimeMillis() - start, null);
            } else {
                // inc exceptionCode Counter
                actionListener.add(JupiterException.SearchMethod_Exception , infoFile);
                log.error(threadId, "Error: error in search Method result Foe json File: " + itemModel.getJupiterPath());
                try {
                    throw new JupiterException("Error: error in search Method result", JupiterException.SearchMethod_Exception);
                } catch (JupiterException e) {
                    log.error(threadId, "Error: error in search Method result Foe json File : " + itemModel.getJupiterPath());
                    log.LogFunctionExecution(this.threadId, "End createFoldersAndDocuments Methods ", System.currentTimeMillis() - start, e);
                    // print stack Message
                    e.printStackTrace();
                    // skip iteration TO Next Item
                    return;
                }
            }
        });
        // prine all statics
        // save Into Action Listener if exists Update It
        com.asset.jupiter.JUPITER.Model.Entities.ActionListener actionListenerInserted = actionListenerService.saveAndFlush(new com.asset.jupiter.JUPITER.Model.Entities.ActionListener(
                new ActionListenerPK(infoFile.getPath(), infoFile.getInfoId()),
                new BigDecimal(this.actionListener.getFailureDocuments()),
                new BigDecimal(this.actionListener.getFailureFolders()),
                new BigDecimal(this.actionListener.getSuccessDocuments()),
                new BigDecimal(this.actionListener.getSuccessFolders()),
                Thread.currentThread().getName()
        ));
        // if error insert
        if (actionListenerInserted == null) {

            // mail and throw Exception
            // send mail
            try {
                sendMail.prepareAndSendTemplate(new mail("", "",  infoFile.getPath().substring(infoFile.getPath().lastIndexOf("\\") + 1, infoFile.getPath().lastIndexOf(".")) + " Error Insert into actionListener Record ","There are" +
                        " Exceptions that Occured During processing the following json File:  " + infoFile.getPath() +
                        " Error Insert into actionListener Record " +
                        "kindly Check Jupiter Tool Log File for details " +
                        "Time : " + new Date()), Defines.errorImage , true , infoFile.getPath());
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            // throw Exception
            try {
                throw new JupiterException("Error Insert into actionListener Record with Path :  " + infoFile.getPath(), JupiterException.CreateActionListener_Exception);
            } catch (JupiterException e) {
                // rollback For Item Folder
                e.printStackTrace();
            }
        } else {

            // send Mail after print Action Listener
            try {
                sendMail.prepareAndSendTemplateWithTable(new mail("",
                        "",
                        infoFile.getPath().substring(infoFile.getPath().lastIndexOf("\\") + 1, infoFile.getPath().lastIndexOf(".")),
                        "Thread Id : " + threadId + " Has Been Finished processing the json File path : " + path + " On : " + new Date()
                ), foldersCount.get(), documentsCount.get(),
                        this.actionListener.getSuccessFolders(),
                        this.actionListener.getSuccessDocuments(),
                        this.actionListener.getFailureFolders(),
                        this.actionListener.getFailureDocuments(),
                        Defines.statusImage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            // for each Exception And Inserted into Database
            HashMap<Integer, Integer> exceptionCount = this.actionListener.getExceptionCount();
            // integration Path
            String integrationFolderPath = config.getIntegrationFolderPath();

            // there are not exception
            // change status success
            if (exceptionCount.size() == 0) {
                infoFile.setStatus(Defines.finished);
                infoFile.setFinishStatus("Success");
                // set current Time
                infoFile.setFinishDate(new Timestamp(new Date().getTime()));

                // update Database
                infoFileService.updateInfoFile(infoFile);

                // move to finish Folder
                // after create Jup File
                /*
                 * move This File To This Finished Folder .
                 */
                // if folder Exists
                // move this File to Finished Folder
                Path temp = null;
                try {
                    String newFile = integrationFolderPath + "\\Finished\\" + infoFile.getPath().substring(infoFile.getPath().lastIndexOf("\\") + 1, infoFile.getPath().lastIndexOf(".")) + "-FINISHED.json";
                    temp = Files.copy(Paths.get(infoFile.getPath()),
                            Paths.get(newFile), StandardCopyOption.REPLACE_EXISTING);

//                                    delete This File
                    File file1 = new File(infoFile.getPath());
                    if (file1.exists()) {
                        Files.deleteIfExists(Paths.get(infoFile.getPath()));
                    }
//                                    System.out.println("File deleted: ");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (temp != null) {
                } else {
                }


            } else {

                // copy error File to errors Folder
                // mmarey
                // if folder Exists
                // move this File to failed Folder
                Path temp = null;
                try {
                    String newFile = integrationFolderPath + "\\Contain_Errors\\" + infoFile.getPath().substring(infoFile.getPath().lastIndexOf("\\") + 1, infoFile.getPath().lastIndexOf(".")) + "-FAILED.json";
                    temp = Files.copy(Paths.get(infoFile.getPath()),
                            Paths.get(newFile), StandardCopyOption.REPLACE_EXISTING);

//                                    delete This File
                    File file1 = new File(infoFile.getPath());
                    if (file1.exists()) {
                        Files.deleteIfExists(Paths.get(infoFile.getPath()));
                    }
//                                    System.out.println("File deleted: ");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (temp != null) {
                } else {
                }

            }

            this.actionListener.getExceptionCount().forEach((exCode, exCount) -> {

                // insert into Exceptions por update if exists
                com.asset.jupiter.JUPITER.Model.Entities.Exception exceptionInserted = exceptionService.saveAndFlush(new com.asset.jupiter.JUPITER.Model.Entities.Exception(
                        // ex Id , infoId
                        new ExceptionPK(exCode, infoFile.getInfoId()),
                        // count
                        new BigDecimal(exCount)
                ));

                // if error
                if (exceptionInserted == null) {
                    // send mail
                    try {
                        sendMail.prepareAndSendTemplate(new mail("", "", infoFile.getPath().substring(infoFile.getPath().lastIndexOf("\\") + 1, infoFile.getPath().lastIndexOf(".")) + " Error Insert into Exception Record " ,
                                "There are Exceptions that Occured During processing the following json File:  " + infoFile.getPath() +
                                        " Error Insert into Exception Record " +
                                        "kindly Check Jupiter Tool Log File for details " +
                                        "Time : " + new Date()), Defines.errorImage , true , infoFile.getPath());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    // throw Exception
                    try {
                        throw new JupiterException("Error Insert into Exception Record with Path :  " + infoFile.getPath(), JupiterException.CreateException_Exception);
                    } catch (JupiterException e) {
                        // rollback For Item Folder
                        e.printStackTrace();
                    }
                    return;
                }

            });
            // rest Action Listener Values
            this.actionListener.setFailureDocuments(0);
            this.actionListener.setFailureFolders(0);
            this.actionListener.setSuccessDocuments(0);
            this.actionListener.setSuccessFolders(0);
            this.actionListener.setExceptionCount(new HashMap<>());
        }

        System.out.println("Elapsed time: " + (System.currentTimeMillis() - start));

    }

    //    @Async("jsonAsyncExecutor")
    // create Folders and sub Documents
    public void createFoldersAndDocuments(LinkedHashMap<Integer, FolderModel> folders,
            Item parentItems,
            String pysicalPath, InfoFile infoFile,
            int foldersCount,
            int documentsCount) {
        log.debug(this.threadId, "Start createFoldersAndDocuments ");
        long startTime = System.currentTimeMillis();
        // save into database int jsonFile Table , if exists Update Only
        JsonFile jsonFileInserted = jsonFileService.saveAndFlush(new JsonFile(new JsonFilePK(infoFile.getPath(), infoFile.getInfoId()),
                new BigDecimal(documentsCount),
                new BigDecimal(foldersCount),
                Defines.createMode,
                this.threadId));
        if (jsonFileInserted == null) {
            // throw Exception And Send Mail
            // send mail
            try {
                sendMail.prepareAndSendTemplate(new mail("", "", infoFile.getPath().substring(infoFile.getPath().lastIndexOf("\\") + 1, infoFile.getPath().lastIndexOf(".")) +" Error Insert into JsonFile Record "  ,
                        "There are Exceptions that Occured During processing the following json File:  " + infoFile.getPath() +
                                " Error Insert into JsonFile Record " +
                                "kindly Check Jupiter Tool Log File for details " +
                                "Time : " + new Date()), Defines.errorImage , true , infoFile.getPath());
            } catch (MessagingException e) {
                log.error(threadId, "error in send mail", e);
                e.printStackTrace();
            }
            // throw Exception
            try {
                throw new JupiterException("Error Insert into jsonFile Record with Path: " + infoFile.getPath(), JupiterException.CreateJsonFile_Exception);
            } catch (JupiterException e) {
                // rollback For Item Folder
                log.error(threadId, "error when insert into JSONFile record with path: " + infoFile.getPath(), e);
                e.printStackTrace();
            }
        }
        // iterate java 8 for each Folders
        folders.forEach((key, value) -> {
            // get folder
            FolderModel folderModel = value;
            // indexClass Folder
            String indexClass = folderModel.getIndexclass();
            log.debug(this.threadId, "start create Folder on IndexClass : " + indexClass);
            // metadata Folder
            HashMap<String, String> folderModelMetadata = folderModel.getMetadata();
            log.debug(this.threadId, "start create Folder on metaData: " + folderModelMetadata);
            //Log.debug(this.threadId, "start create Folder on metaData : " + folderModelMetadata);
            // documents Inside Folder
            LinkedHashMap<Integer, DocumentModel> folderModelDocuments = folderModel.getDocuments();
            log.info(this.threadId, "start Search in IndexClass for folder");
            // search IndexClass For Folder and get metadata  dynamic Table
            Object[] objects = new Object[0];
            try {
                objects = searchIndexClass(folderModel, null, Defines.folderItem , infoFile);
            } catch (JupiterException e) {
                log.error(this.threadId, "error when search IndexClass for folder", e);
                actionListener.setFailureFolders(actionListener.getFailureFolders() + 1);
                e.printStackTrace();
                errorIndexClassSearch(infoFile, Defines.folderItem);
                // Add this folder in created failed json file
                log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                return;
            }
            // if label != null
            if (objects == null) {
                // inc exceptionCode Counter
                actionListener.add(JupiterException.SearchIndexClass_Exception , infoFile);
                // log & throw exception

                try {
                    throw new JupiterException("Error While search IndexClass Method", JupiterException.SearchIndexClass_Exception);
                } catch (JupiterException e) {
                    // increment failure
                    actionListener.setFailureFolders(actionListener.getFailureFolders() + 1);
                    log.error(threadId, "Error on start Search IndexClass for folder", e);
                    e.printStackTrace();
                    // break iteration
                    log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                    return;
                }
            }
            // if label != null
            String labelFolder = (String) objects[0];
            if(isNullOrEmpty(labelFolder)){
                log.error(this.threadId,"error can not create folder with empty label field");
                log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, new JupiterException("can not create folder with empty label field"));
                return;
            }
            // if indexclass != null
            Indexclass indexclass = (Indexclass) objects[1];
            // if columnValues != null
            ArrayList columnValues = (ArrayList) objects[2];
            // if tablelabel != null
            String tableLabel = (String) objects[3];
            log.debug(this.threadId, "data after search IndexClass : \n"
                    + " labelFolder : " + labelFolder
                    + "\n indexClass : " + indexClass
                    + "\n  columnValues  : " + columnValues);
            // insert Folder Into Database item as a folder
            // create Folder
            // insert item into database
            log.info(this.threadId, "Start Create New Folder as a Item");
            Item newFolderItem = null;
            try {
                newFolderItem = createNewItem(parentItems,
                        Defines.folderItem,
                        indexclass,
                        pysicalPath,
                        "",
                        labelFolder,
                        folderModelMetadata, infoFile);
            } catch (JupiterException e) {
                // increment failure
                actionListener.setFailureFolders(actionListener.getFailureFolders() + 1);
                log.error(this.threadId, "error in create new Item as folder in jupiter", e);
                e.printStackTrace();
                // break This iteration
                log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                return;
            }
            if (newFolderItem != null) {
                log.info(this.threadId, "Done Create New Folder with Id: " + newFolderItem.getItemid());
                log.info(this.threadId, "Start Create New Folder as a Media");
                // insert media
                boolean b = false;
                try {
                    Object[] newMedia = createNewMedia(newFolderItem, Defines.folderItem,
                            pysicalPath, parentItems, infoFile);
                    b = (boolean) newMedia[0];
                } catch (JupiterException e) {
                    // increment failure
                    actionListener.setFailureFolders(actionListener.getFailureFolders() + 1);
                    log.error(threadId, "error in create new document media", e);
                    e.printStackTrace();
                    // break This iteration
                    log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                    return;
                }
                // if done created new item and media
                if (b) {
                    log.debug(this.threadId, "Done create Item media with id : " + newFolderItem.getItemid());
                    // last step to create item Insert into dynamic Table
                    // set item Id to column values
                    columnValues.set(0, newFolderItem.getItemid());
                    boolean doneInserted = false;
                    try {
                        doneInserted = insertIntoDynamicTable(tableLabel,
                                columnValues,
                                indexclass, folderModelMetadata , infoFile);
                    } catch (JupiterException e) {
                        // increment failure
                        actionListener.setFailureFolders(actionListener.getFailureFolders() + 1);
                        log.error(this.threadId, "error when insert into folder indexclass table", e);
                        // change Db , Excel Status
                        changeErrorStatus(infoFile);
                        log.debug(this.threadId, "start rollback process");
                        // rollback create ITem
                        rollBackJupiter4.rollBack(newFolderItem, new AccessrulePK(newFolderItem.getItemid(),
                                newFolderItem.getType().longValue(),
                                1, 1),
                                new LinkPK(newFolderItem.getItemid(),
                                        parentItems.getItemid(),
                                        parentItems.getType().longValue()),
                                new ItemIndexclassPK(newFolderItem.getItemid(), newFolderItem.getIndexclass()),
                                new ItemscountPK(parentItems.getItemid(),
                                        parentItems.getType().longValue(),
                                        newFolderItem.getIndexclass()));
                        log.debug(this.threadId, "done rollback jupiter schema");
                        // rollback create Media
                        rollBackJupiter4Med.rollBack(newFolderItem.getType().toString(),
                                null,
                                "media" + newFolderItem.getItemid().replaceAll("\\D+", ""));
                        log.debug(this.threadId, "done rollback media schema");
                        e.printStackTrace();
                        log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                        // continue next operation
                        return;
                    }
                    // if Done insert Folder Into Jupiter
                    if (doneInserted) {
                        log.info(this.threadId, "Done Insert Folder TO Jupiter ");
                        // update success Folders Creates
                        actionListener.setSuccessFolders(actionListener.getSuccessFolders() + 1);
                    }
                    log.info(this.threadId, "Done Item Insert into dynamic table ");
                    log.info(this.threadId, "Start Insert Items inside folders: " + folderModelDocuments);
                    // iterate java 8 for each document inside Folder
                    Item finalNewFolderItem = newFolderItem;
                    folderModelDocuments.forEach((documentsKey, documentsValue) -> {
                        // location Id
                        String globalLocationId;
                        Mediaserver globalmediaserver;
                        DocumentModel documentModel = documentsValue;
                        String documentIndexClass = documentModel.getIndexclass();
                        String documentPath = documentModel.getPath();
                        HashMap<String, String> documentMetaData = documentModel.getMetadata();
                        log.debug(this.threadId, "Start Create New Document as a Item. Path " + documentPath
                                + "\n indexClass: " + documentIndexClass
                                + "\n metadata: " + documentMetaData);
                        log.info(this.threadId, "Start Search IndexClass For item Document . ");
                        // search IndexClass For Item and get metadata  dynamic Table
                        Object[] objects1 = new Object[0];
                        try {
                            objects1 = searchIndexClass(null, documentModel, Defines.documentItem , infoFile);
                        } catch (JupiterException e) {
                            // increment failure
                            actionListener.setFailureDocuments(actionListener.getFailureDocuments() + 1);
                            log.error(this.threadId, "errpr in search in INDEXCLASS for item document", e);
                            e.printStackTrace();
                            // error In IndexClass Search
                            errorIndexClassSearch(infoFile, Defines.documentItem);
                            // Add this folder in created failed json file
                            // break
                            log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                            return;
                        }
                        // if label != null
                        if (objects1 == null) {
                            // inc exceptionCode Counter
                            actionListener.add(JupiterException.SearchIndexClass_Exception , infoFile);
                            // log & throw exception
                            //log.error(this.threadId, "start Search IndexClass for Item Document  ");
                            try {
                                throw new JupiterException("Error While search IndexClass Method For Folder : ", JupiterException.SearchIndexClass_Exception);
                            } catch (JupiterException e) {
                                // increment failure
                                actionListener.setFailureDocuments(actionListener.getFailureDocuments() + 1);
                                log.error(this.threadId, "error when search indexclass methos for folder", e);
                                e.printStackTrace();
                                log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                                // continue next Folder
                                return;
                            }
                        }
                        // if label != null
                        String labelDocument = (String) objects1[0];
                        if(isNullOrEmpty(labelDocument)){
                            log.error(this.threadId,"error can not create document with empty label field");
                            log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, new JupiterException("can not create document with empty label field"));
                            return;
                        }
                        // if indexclass != null
                        Indexclass indexclassDocument = (Indexclass) objects1[1];
                        // if columnValues != null
                        ArrayList columnValuesDocument = (ArrayList) objects1[2];
                        // if tablelabel != null
                        String tableLabelDocument = (String) objects1[3];
                        log.debug(this.threadId, "data after search IndexClass : \n"
                                + " labelFolder : " + labelDocument
                                + "\n indexClass : " + indexclassDocument
                                + "\n  columnValues  : " + columnValuesDocument);

                        log.info(this.threadId, "Start Create New Document as a Item  ");
                        // create new Document Item
                        // insert item into database
                        Item newDocItem = null;
                        try {
                            newDocItem = createNewItem(finalNewFolderItem,
                                    Defines.documentItem,
                                    indexclassDocument,
                                    pysicalPath,
                                    documentPath,
                                    labelDocument,
                                    documentMetaData, infoFile);
                        } catch (JupiterException e) {
                            // increment failure
                            actionListener.setFailureDocuments(actionListener.getFailureDocuments() + 1);
                            log.error(threadId, "error when create new item as document", e);
                            e.printStackTrace();
                            log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                            // break  This iteration
                            return;
                        }
                        if (newDocItem != null) {
                            log.debug(this.threadId, "Done Create New Item as a document Id: " + newDocItem.getItemid());
                            log.info(this.threadId, "Start Create New Document Media");
                            // insert media
                            // if not end / add /
                            String finalPysicalPath = (!pysicalPath.substring(pysicalPath.length() - 1).equalsIgnoreCase(Defines.fileSeparation)) ? finalPysicalPath = pysicalPath + Defines.fileSeparation : pysicalPath;
                            // document Full Path = physical + doc path
                            String fullItemPath = finalPysicalPath + documentPath;
                            boolean insertedDoc = false;
                            try {
                                Object[] newMedia = createNewMedia(newDocItem, Defines.documentItem,
                                        fullItemPath, finalNewFolderItem, infoFile);
                                insertedDoc = (boolean) newMedia[0];
                                globalLocationId = (String) newMedia[1];
                                globalmediaserver = (Mediaserver) newMedia[2];
                            } catch (JupiterException e) {
                                // increment failure
                                actionListener.setFailureDocuments(actionListener.getFailureDocuments() + 1);
                                log.error(threadId, "error when create document media: " + newDocItem, e);
                                e.printStackTrace();
                                // break This iteration
                                log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                                return;
                            }
                            if (insertedDoc) {
                                log.info(this.threadId, "Done Create New Document Media. Id:" + newDocItem.getItemid() + " Full Path: " + fullItemPath);
                                //                 throw exception
                                // commit to database saved
//                                transactionManager.commit(TransactionAspectSupport.currentTransactionStatus());
                                // last step to create item Insert into dynamic Table
                                // set item Id to column values
                                columnValuesDocument.set(0, newDocItem.getItemid());
                                boolean doneInsertedDocument = false;
                                try {
                                    log.info(threadId, "inser docuemt metadata in dynamic table");
                                    doneInsertedDocument = insertIntoDynamicTable(
                                            tableLabelDocument, columnValuesDocument,
                                            indexclassDocument,
                                            documentMetaData , infoFile);
                                } catch (JupiterException e) {
                                    // increment failure
                                    actionListener.setFailureDocuments(actionListener.getFailureDocuments() + 1);
                                    // change Db , Excel Status
                                    changeErrorStatus(infoFile);
                                    log.error(this.threadId, "error when insert document metadata", e);
                                    // rollback create ITem
                                    log.debug(threadId, "start rollback jupiter schema process");
                                    rollBackJupiter4.rollBack(newDocItem, new AccessrulePK(newDocItem.getItemid(),
                                            newDocItem.getType().longValue(),
                                            1, 1),
                                            new LinkPK(newDocItem.getItemid(),
                                                    finalNewFolderItem.getItemid(),
                                                    finalNewFolderItem.getType().longValue()),
                                            new ItemIndexclassPK(newDocItem.getItemid(), newDocItem.getIndexclass()),
                                            new ItemscountPK(finalNewFolderItem.getItemid(),
                                                    finalNewFolderItem.getType().longValue(),
                                                    newDocItem.getIndexclass()));
                                    // rollback create Media
                                    log.debug(threadId, "start rollback media schema process");
                                    rollBackJupiter4Med.rollBack(newDocItem.getType().toString(),
                                            new MediamanagerPK(newDocItem.getItemid(),
                                                    newDocItem.getType().longValue()),
                                            "media" + newDocItem.getItemid().replaceAll("\\D+", ""));

                                    e.printStackTrace();
                                    log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                                    // continue next operation
                                    return;
                                }
                                // if Done insert document To Jupiter
                                if (doneInsertedDocument) {
                                    log.info(this.threadId, "Done Insert Document TO Jupiter ");
                                    actionListener.setSuccessDocuments(actionListener.getSuccessDocuments() + 1);
                                }
                                log.info(this.threadId, "Create New Item Media***");
                                // create Jup File
                                // 1 -  physical Path --> jupiterPath
                                // 2 -  new File --> path File in disk
                                JupFileBuilder jupFile = new JupFileBuilder();
                                FileManager fileManager = new FileManager();
                                //"D:\\+"
                                jupFile.createFiles(globalmediaserver.getAddress() + "\\", fileManager.readBytesFromFile(
                                        new File(fullItemPath)),
                                        globalLocationId,
                                        newDocItem.getExt());

                            } else {
                                // inc exceptionCode Counter
                                actionListener.add(JupiterException.CreateMedia_Exception , infoFile);
                                try {
                                    throw new JupiterException("Error Insert document as Media In database", JupiterException.CreateMedia_Exception);
                                } catch (JupiterException e) {
                                    // increment failure
                                    actionListener.setFailureDocuments(actionListener.getFailureDocuments() + 1);
                                    log.error(threadId, "Error Insert document Media In database ", e);
                                    // change Db , Excel Status
                                    changeErrorStatus(infoFile);
                                    log.info(threadId, "start rollback process");
                                    // rollback createNew Item
                                    rollBackJupiter4.rollBack(newDocItem, new AccessrulePK(newDocItem.getItemid(),
                                            newDocItem.getType().longValue(),
                                            1, 1),
                                            new LinkPK(newDocItem.getItemid(),
                                                    finalNewFolderItem.getItemid(),
                                                    finalNewFolderItem.getType().longValue()),
                                            new ItemIndexclassPK(newDocItem.getItemid(), newDocItem.getIndexclass()),
                                            new ItemscountPK(finalNewFolderItem.getItemid(),
                                                    finalNewFolderItem.getType().longValue(),
                                                    newDocItem.getIndexclass()));
                                    e.printStackTrace();
                                    log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                                    return;
                                }
                            }
                        } else {
                            // inc exceptionCode Counter
                            actionListener.add(JupiterException.CreateItem_Exception , infoFile);
                            try {
                                throw new JupiterException("Error Insert document as Item In database ", JupiterException.CreateItem_Exception);
                            } catch (JupiterException e) {
                                // increment failure
                                actionListener.setFailureDocuments(actionListener.getFailureDocuments() + 1);
                                log.error(threadId, "Error Insert document as Item In database ", e);
                                e.printStackTrace();
                                // skip iteration For This Item
                                log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                                return;
                            }
                        }
                    });
                } else {
                    // inc exceptionCode Counter
                    actionListener.add(JupiterException.CreateMediaFolder_Exception , infoFile);
                    // increment failure
                    actionListener.setFailureFolders(actionListener.getFailureFolders() + 1);
                    try {
                        throw new JupiterException("Error Insert Folder as media In database ", JupiterException.CreateMediaFolder_Exception);
                    } catch (JupiterException e) {
                        log.error(threadId, "Error Insert Folder as media In database ", e);
                        // rollback For Item Folder
                        e.printStackTrace();
                    }
                }
            } else {
                // inc exceptionCode Counter
                actionListener.add(JupiterException.CreateItemFolder_Exception , infoFile);
                // increment failure
                actionListener.setFailureFolders(actionListener.getFailureFolders() + 1);
                try {
                    throw new JupiterException("Error Insert Folder as Item In database ", JupiterException.CreateItemFolder_Exception);
                } catch (JupiterException e) {
                    Log.error(threadId, "Error Insert Folder as Item In database", e);
                    e.printStackTrace();
                    log.LogFunctionExecution(this.threadId, "createFoldersAndDocuments", System.currentTimeMillis() - startTime, e);
                    // break This iteration
                    return;
                }
            }
        });
    }

    // last step to insert item (doc or folder)
    /**
     * function to insert into dynamic table last step to insert item (doc or
     * folder)
     *
     * @param tableName
     * @param columnValues
     * @param indexclass
     * @param indexClassMetaData
     * @return
     * @throws JupiterException
     */
    public boolean insertIntoDynamicTable(String tableName,
            ArrayList columnValues,
            Indexclass indexclass,
            HashMap<String, String> indexClassMetaData , InfoFile infoFile
    ) throws JupiterException {
        long startTime = System.currentTimeMillis();
        // get result And Structure Of Table
        ArrayList<String> dynamicTableInfo1 = null;
        try {
            dynamicTableInfo1 = dynamicService.getDynamicTableInfo(tableName.toUpperCase(), config.getJUPITER_SCHEMA());
        } catch (SQLException e) {
            // increment error code
            actionListener.add(JupiterException.SQL_Exception , infoFile);
            log.error(threadId, "Error In Get IndexClass Table Structure", e);
            log.LogFunctionExecution(threadId, "insertIntoDynamicTable", System.currentTimeMillis() - startTime, e);
            throw new JupiterException("Error In Get IndexClass Table Structure  ", JupiterException.SQL_Exception);
        }
        // if error
        if (dynamicTableInfo1 == null || dynamicTableInfo1.isEmpty()) {
            log.error(threadId, "Error Get IndexClass Table Structure");
            log.LogFunctionExecution(threadId, "insertIntoDynamicTable", System.currentTimeMillis() - startTime, new JupiterException("Error Get IndexClass Table Structure  "));
            throw new JupiterException("Error Get IndexClass Table Structure  ");
        } else {
            // continue
            // insert MetaDate Into Dynamic Table
            try {
                boolean b = dynamicService.insertDynamicTableInfo(tableName.toUpperCase(),
                        dynamicTableInfo1, columnValues, indexclass,
                        indexClassMetaData);

                log.info(this.threadId, "Done Insert into DynamicTable Database: " + b);
                log.LogFunctionExecution(threadId, "insertIntoDynamicTable", System.currentTimeMillis() - startTime, null);
            } catch (SQLException e) {
                // increment error code
                actionListener.add(JupiterException.SQL_Exception , infoFile);
                log.error(threadId, "SQLException into insertDynamicTableInfo: ", e);
                log.LogFunctionExecution(threadId, "insertIntoDynamicTable", System.currentTimeMillis() - startTime, e);
                throw new JupiterException("SQLException : " + e.getMessage(), JupiterException.SQL_Exception);
            }
        }
        return true;
    }

    /**
     * function ti search in folder indexclass
     *
     * @param folderModel
     * @param documentModel
     * @param type
     * @return
     * @throws JupiterException
     */
    public Object[] searchIndexClass(FolderModel folderModel, DocumentModel documentModel, String type , InfoFile infoFile) throws JupiterException {
        long startTime = System.currentTimeMillis();
        // type = folder or document
        Object[] result = new Object[4];
        // check Type
        switch (type) {
            case Defines.folderItem:
                log.info(this.threadId, "search Index Class for Folder ");
                String indexClassFolder = folderModel.getIndexclass();
                HashMap<String, String> metadataFolder = folderModel.getMetadata();
                result = checkIndexClassAndLabel(indexClassFolder, metadataFolder , infoFile);
                break;
            case Defines.documentItem:
                log.info(this.threadId, "search Index Class for Document  ");
                String indexClassDocument = documentModel.getIndexclass();
                HashMap<String, String> metadataDocument = documentModel.getMetadata();
                result = checkIndexClassAndLabel(indexClassDocument, metadataDocument ,infoFile );
                break;
            default:
                // log & throw Exception
                break;
        }
        // check If Null
        log.LogFunctionExecution(threadId, "searchIndexClass", System.currentTimeMillis() - startTime, null);
        return result;
    }

    /**
     * check IndexClass And Get Label
     *
     * @param indexClass
     * @param metadata
     * @return
     * @throws JupiterException
     */
    public Object[] checkIndexClassAndLabel(String indexClass, HashMap<String, String> metadata , InfoFile infoFile) throws JupiterException {
        long startTime = System.currentTimeMillis();
        ArrayList columnValues = new ArrayList();
        columnValues.add("");
        columnValues.add(0);
        log.debug(this.threadId, "check IndexClassAndLabel IndexClass : " + indexClass + "  metaData : " + metadata);
        Object arra[] = new Object[4];
        try {
            // select IndexClass By IndexClassName
            CompletableFuture<Indexclass> byClassname = indexclassService.findByClassname(indexClass);
            Indexclass indexclass = byClassname.get();
            // if exists
            if (indexclass != null) {
                log.debug(this.threadId, "check IndexClassAndLabel Item Index ClassId: " + indexclass.getClassid());
                // get table indexClass
                CompletableFuture<Table> byId = tablesService.findById(indexclass.getPrimarytableid());
                Table table = byId.get();
                if (table != null) {
                    log.debug(this.threadId, " check IndexClassAndLabel get Table Of IndexClass Id: " + table.getId());
                    // get Label Name From Fields where fieldId = ? and tableId= ?
                    CompletableFuture<Field> allByTableIdAndFieldid = fieldService.findAllByTableIdAndFieldid(table.getId(), indexclass.getLabelfieldid());
                    Field field = allByTableIdAndFieldid.get();
                    if (field != null) {
                        log.debug(this.threadId, "check IndexClassAndLabel Field is: " + field.getName());
                        // get view Field IndexClass Not field table Name
                        // select DisplayName from
                        CompletableFuture<Viewfield> viewFieldById = viewFieldService.findById(new ViewfieldPK(indexclass.getDefviewid(),
                                indexclass.getLabelfieldid()));
                        Viewfield viewfield = viewFieldById.get();
                        // set label index 0
                        String label = metadata.get(viewfield.getDisplayname());
                        if (label != null) {
                            arra[0] = label;
                            // set indexClass Object index 1
                            arra[1] = indexclass;
                            // set to column values index 2
                            arra[2] = columnValues;
                            // set to Table Name index 3
                            arra[3] = table.getTablename();
                            log.debug(this.threadId, "Done search IndexClass Label");
                        }
//                        }
                    } else {
                        // inc exceptionCode Counter
                        actionListener.add(JupiterException.FieldLabel_Exception , infoFile);
                        log.error(threadId, "Error In check IndexClassAndLabel In getFielsd from database Not Found");
                        log.LogFunctionExecution(threadId, "checkIndexClassAndLabel", System.currentTimeMillis() - startTime, new JupiterException("Error In check IndexClassAndLabel In getFielsd from database ", JupiterException.FieldLabel_Exception));
                        throw new JupiterException("Error In check IndexClassAndLabel In getFielsd from database ", JupiterException.FieldLabel_Exception);
                    }
                } else {
                    // inc exceptionCode Counter
                    actionListener.add(JupiterException.TableId_Exception , infoFile);
                    log.error(threadId, "Error In check IndexClassAndLabel In gettable from database Not Found " + indexclass.getPrimarytableid());
                    log.LogFunctionExecution(threadId, "checkIndexClassAndLabel", System.currentTimeMillis() - startTime, new JupiterException("Error In check IndexClassAndLabel In gettable from database " + indexclass.getPrimarytableid(), JupiterException.TableId_Exception));
                    throw new JupiterException("Error In check IndexClassAndLabel In gettable from database " + indexclass.getPrimarytableid(), JupiterException.TableId_Exception);
                }
            } else {
                // inc exceptionCode Counter
                actionListener.add(JupiterException.IndexClassName_Exception , infoFile);
//                indexclass
                log.error(threadId, "Error In check IndexClassAndLabel In indexclass from database Not Found " + indexClass);
                JupiterException e = new JupiterException("Error In check IndexClassAndLabel In indexclass from database " + indexClass, JupiterException.IndexClassName_Exception);
                log.LogFunctionExecution(threadId, "checkIndexClassAndLabel", System.currentTimeMillis() - startTime, e);
                throw e;
            }
        } catch (InterruptedException e) {
            // increment error code
            actionListener.add(JupiterException.Interrupted_Exception , infoFile);
            log.error(threadId, "Error In check IndexClassAndLabel Method : InterruptedException " + e.getMessage());
            log.LogFunctionExecution(threadId, "checkIndexClassAndLabel", System.currentTimeMillis() - startTime, e);
            throw new JupiterException("InterruptedException : " + e.getMessage(), JupiterException.Interrupted_Exception);
        } catch (ExecutionException e) {
            // increment error code
            actionListener.add(JupiterException.Execution_Exception , infoFile );
            log.error(threadId, "Error In check IndexClassAndLabel Method : ExecutionException " + e.getMessage());
            log.LogFunctionExecution(threadId, "checkIndexClassAndLabel", System.currentTimeMillis() - startTime, e);
            throw new JupiterException("ExecutionException : " + e.getMessage(), JupiterException.Execution_Exception);
        }
        log.LogFunctionExecution(threadId, "checkIndexClassAndLabel", System.currentTimeMillis() - startTime, null);
        return arra;
    }

    /*
    maybe Folder , maybe Document
     * 1 - create access Template
     * 2 - create itemModel
     *3 - insert item into database
     * 4 -
     * */
    public Item createNewItem(Item itemModelParent,
            String type,
            Indexclass indexClass,
            String pysicalPath,
            String path,
            String label,
            HashMap<String, String> folderModelMetadata,
            InfoFile infoFile) throws JupiterException {
        long startTime = System.currentTimeMillis();
        // calc DocumentSize
        long documentSize = 0;
        // iten extension
        String extItem = "2";
        // num of pages
        int multiPageCount = 0;
        // item type -- 2 folder 3 doc
        int typeItem = 2;
        // if not end / add /
        pysicalPath = !pysicalPath.substring(pysicalPath.length() - 1).equalsIgnoreCase(Defines.fileSeparation) ? pysicalPath + Defines.fileSeparation : pysicalPath;
        // document Full Path = physical + doc path
        String fullItemPath = pysicalPath + path;
        // check if signed
        int isSigned = 0;
        Item itemInserted = null;
//        try {
        // if item is Document
        if (type.equalsIgnoreCase(Defines.documentItem)) {
            // check File In Disk
            boolean errorDocumentPath = errorDocumentPath(infoFile, fullItemPath);
            if (!errorDocumentPath) {
                return null;
            }
            log.info(this.threadId, "Item is a Document ");
            // set type 3
            typeItem = 3;
            //  to get extension from text
            extItem = FilenameUtils.getExtension(path);
            if (extItem.equalsIgnoreCase("pdf") || extItem.equalsIgnoreCase("tiff")) {
                // calculate MultiPageCount by itext
                log.info(this.threadId, "Item is a Document is pdf or tiff");
                PdfReader reader = null;
                try {
                    // reader to get page Count
                    reader = new PdfReader(fullItemPath);
                } catch (IOException e) {
                    // increment error code
                    actionListener.add(JupiterException.IO_EXCEPTION , null);
                    log.error(threadId, "Error While PdfReader : ", e);
                    log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                    throw new JupiterException("IOException : " + e.getMessage(), JupiterException.IO_EXCEPTION);
                }
                // check if only pdf to check signed and signed is enable
                if (extItem.equalsIgnoreCase("pdf") && useDigitalSignature == 1) {
                    log.info(this.threadId, "Digital Signature Enabled on Items ------");
                    try {
                        // method to check PDf Signed
                        boolean b = isPdfSigned(reader);
                        // if signed set is signed = 1
                        if (b) {
                            isSigned = 1;
                        }
                    } catch (IOException e) {
                        // increment error code
                        actionListener.add(JupiterException.IO_EXCEPTION , null);
                        log.error(threadId, "Error While Check signed Document : " + e.getMessage());
                        log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                        throw new JupiterException("IOException : " + e.getMessage(), JupiterException.IO_EXCEPTION);
                    }
                }
                multiPageCount = reader.getNumberOfPages();
                reader.close();
            }
            // calculate Length Of Document
            File file = new File(fullItemPath);
            if (file.exists()) {
                // set document Size to variable
                documentSize = file.length();
                log.debug(this.threadId, "Document Size is : " + documentSize);
            } else {
                // increment error code
                actionListener.add(JupiterException.FILE_NOT_EXIST_EXCEPTION , infoFile);
                log.error(threadId, "Error File Not Found ");
                JupiterException e = new JupiterException("Error File Not Found ", JupiterException.FILE_NOT_EXIST_EXCEPTION);
                log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                throw e;
            }
        }
        log.info(this.threadId, "Item Info:extItem " + extItem + " multiPageCount " + multiPageCount + " typeItem " + typeItem + " fullItemPath " + fullItemPath + "  documentSize " + documentSize + " isSigned " + isSigned);
        // get next item From sequence item Table
        String lastItem = null;
        try {
            lastItem = itemsService.findLastItem().get();
        } catch (InterruptedException e) {
            log.error(threadId, "error in get new itemid from sequence", e);
            e.printStackTrace();
        } catch (ExecutionException e) {
            log.error(threadId, "error in get new itemid from sequence", e);
            e.printStackTrace();
        }
        log.info(this.threadId, "Get Last Time From Sequence for Id : " + lastItem);
        // current Date
        long currentDate = dateToLong(new Date());
        // next Item To Insert
        int nextItem = Integer.parseInt(lastItem);
        // create access Template Item
        Accesstemplate accessTemplate = new Accesstemplate(
                1, // templateid
                new BigDecimal(30), // defaultaccess
                null, // domainId
                null, // dynamicOwner
                new BigDecimal(1), // ownerid
                "default" //templatename
        );
        // create item
        Item item = new Item(
                Defines.prefixItemId + nextItem, // id
                "0", // annotated
                new BigDecimal(2), //approvalStatus
                new BigDecimal(0), //bookmarkCount
                new BigDecimal(currentDate), //creationdate
                new BigDecimal(1), //creator
                new BigDecimal(documentSize), //documentsize
                extItem, //ext
                new BigDecimal(0), //ftrStatus
                "0", //imagetype
                indexClass.getClassid(), //indexclass
                new BigDecimal(isSigned), //ispdfdigitallysigned
                label, //label
                new BigDecimal(1), //lastmodifiedby
                new BigDecimal(0), //lockcnt
                new BigDecimal(0), //locktype
                indexClass.getMediaserverid(),//mediaserverid
                new BigDecimal(currentDate), //moddate
                new BigDecimal(multiPageCount), //multiPageCount
                new BigDecimal(1), //owner
                "0",//revised
                new BigDecimal(0), //status typeItem
                new BigDecimal(typeItem), //type
                new BigDecimal(0), //usercheckoutid
                null, //version
                accessTemplate //accesstemplate
        );
        //  save Into Database
        CompletableFuture<Item> itemsCompletableFuture = itemsService.save(item);
        // get from async Method
        Item items = null;
        try {
            items = itemsCompletableFuture.get();
        } catch (InterruptedException e) {
            log.error(threadId, "Error Insert item Into database : InterruptedException Exception  " + item.getItemid(), e);
            throw new JupiterException("Error Insert item Into Database : " + item.getItemid() + "   " + e.getMessage(), JupiterException.Interrupted_Exception);
        } catch (ExecutionException e) {
            log.error(threadId, "Error Insert item Into database : ExecutionException Exception  " + item.getItemid(), e);
            throw new JupiterException("Error Insert item Into Database : " + item.getItemid() + "   " + e.getMessage(), JupiterException.Execution_Exception);
        }
        // check If Not null
        if (items != null) {
//                transactionManager.getTransaction();
            log.info(this.threadId, "Done Insert Item Into Database With Label : " + items.getLabel());
            // before Insert link we should create access rules for item
            // implement function to create access rules
            log.info(this.threadId, "Start Create Access Rule For Item ");
            Accessrule accessRuleInserted = null;
            try {
                accessRuleInserted = createAccessRule(items , infoFile);
            } catch (ExecutionException e) {
                // increment error code
                actionListener.add(JupiterException.Access_Rule_Create_EXCEPTION , infoFile);
                log.error(threadId, "Error Insert Into AccessRules Table  database : ExecutionException Exception", e);
                // change Db , Excel Status
                changeErrorStatus(infoFile);
                log.debug(threadId, "start AccessRules rollback process");
                // rollback
                rollBackJupiter4.rollBack(items,
                        null,
                        null,
                        null,
                        null);
                log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                throw new JupiterException("Error Insert AccessRules Into Database : " + e.getMessage(), JupiterException.Execution_Exception);
            } catch (InterruptedException e) {
                // increment error code
                actionListener.add(JupiterException.Access_Rule_Create_EXCEPTION , infoFile);
                log.error(threadId, "Error Insert item Into database : InterruptedException Exception", e);
                // change Db , Excel Status
                changeErrorStatus(infoFile);
                log.debug(threadId, "start AccessRules rollback process");
                // rollback
                rollBackJupiter4.rollBack(items,
                        null,
                        null,
                        null,
                        null);
                log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                throw new JupiterException("Error Insert AccessRules Into Database :  " + e.getMessage(), JupiterException.Interrupted_Exception);
            }
            // check result accessRule
            if (accessRuleInserted == null) {
                // log and Throw Exception
                // incremsent error code
                actionListener.add(JupiterException.Access_Rule_Create_EXCEPTION , infoFile);
                log.error(threadId, "Error in create Access Rule Method when create Item : ");
                // change Db , Excel Status
                changeErrorStatus(infoFile);
                // rollback
                rollBackJupiter4.rollBack(items,
                        null,
                        null,
                        null,
                        null);
                JupiterException e = new JupiterException("Error in create Access Rule Method when create Item : ", JupiterException.Access_Rule_Create_EXCEPTION);
                log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                throw e;
            }
            log.info(this.threadId, "Done insert item Into Access Rules");
            // create linkPk
            LinkPK linkPK = new LinkPK(
                    items.getItemid(), //itemid
                    itemModelParent.getItemid(), //parentid
                    itemModelParent.getType().longValue() // parenttype
            );
            // create Link Entity To Insert Into Database
            // parent type 1 = knowledgepool , 2 = folder
            Link links = new Link(linkPK, // linkPK
                    items.getIndexclass() // Indexclass
                    ,
                     new BigDecimal(0) //pos
            );
            // insert into database
            Link linkSaved = null;
            try {
                linkSaved = linksService.saveAndFlush(links).get();
            } catch (InterruptedException e) {
                // increment error code
                actionListener.add(JupiterException.Links_Create_EXCEPTION , infoFile);
                log.error(threadId, "Error Insert Links Into database : InterruptedException Exception", e);
                // change Db , Excel Status
                changeErrorStatus(infoFile);
                log.debug(threadId, "start links rollback process");
                // rollback
                rollBackJupiter4.rollBack(items,
                        accessRuleInserted.getId(),
                        null,
                        null,
                        null);
                throw new JupiterException("Error Insert Links Into Database :  " + e.getMessage(), JupiterException.Interrupted_Exception);
            } catch (ExecutionException e) {
                // increment error code
                actionListener.add(JupiterException.Links_Create_EXCEPTION , infoFile);
                log.error(threadId, "Error Insert Into Links Table  database : ExecutionException Exception", e);
                // change Db , Excel Status
                changeErrorStatus(infoFile);
                log.debug(threadId, "start links rollback process");
                // rollback
                rollBackJupiter4.rollBack(items,
                        accessRuleInserted.getId(),
                        null,
                        null,
                        null);
                JupiterException ex = new JupiterException("Error Insert Links Into Database : " + e.getMessage(), JupiterException.Execution_Exception);
                log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                throw ex;
            }
            // if Done inserted
            if (linkSaved != null) {
                Log.debug(this.threadId, "Donw Insert Item Info To Links with \n Item Id : " + items.getItemid()
                        + "\n ItemParentId : " + itemModelParent.getItemid()
                        + "\n parent Type : " + itemModelParent.getType()
                        + "\n IndexClass : " + items.getIndexclass());
                // insert into ItemIndex Pk
                ItemIndexclassPK itemIndexclassPK = new ItemIndexclassPK(
                        items.getItemid(),
                        items.getIndexclass()
                );
                // insert item indexClassId
                ItemIndexclass itemIndexClass = new ItemIndexclass(
                        itemIndexclassPK, //itemIndexclassPK
                        item //item
                );
                CompletableFuture<ItemIndexclass> itemIndexclassCompletableFuture = itemIndexclassService.saveAndFlush(itemIndexClass);
                ItemIndexclass itemIndexclass = null;
                try {
                    itemIndexclass = itemIndexclassCompletableFuture.get();
                } catch (InterruptedException e) {
                    // increment error code
                    actionListener.add(JupiterException.ItemIndexClass_Create_EXCEPTION , infoFile);
                    log.error(threadId, "Error Insert ItemIndexclass Into database : InterruptedException Exception", e);
                    // change Db , Excel Status
                    changeErrorStatus(infoFile);
                    log.debug(threadId, "start itemIndexclass rollback process");
                    // rollback
                    rollBackJupiter4.rollBack(items,
                            accessRuleInserted.getId(),
                            linkSaved.getId(),
                            null,
                            null);
                    log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                    throw new JupiterException("Error Insert ItemIndexclass Into Database :  " + e.getMessage(), JupiterException.Interrupted_Exception);
                } catch (ExecutionException e) {
                    // increment error code
                    actionListener.add(JupiterException.ItemIndexClass_Create_EXCEPTION , infoFile);
                    log.error(threadId, "Error Insert Into ItemIndexclass Table  database : ExecutionException Exception", e);
                    // change Db , Excel Status
                    changeErrorStatus(infoFile);
                    log.debug(threadId, "start itemIndexclass rollback process");
                    // rollback
                    rollBackJupiter4.rollBack(items,
                            accessRuleInserted.getId(),
                            linkSaved.getId(),
                            null,
                            null);
                    log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                    throw new JupiterException("Error Insert ItemIndexclass Into Database : " + e.getMessage(), JupiterException.Execution_Exception);
                }
                //
                if (itemIndexclass != null) {
                    // Done Insert into IndexClass                    
                    log.debug(this.threadId, "Done insert item Into itemIndexclass Id : " + itemIndexclass.getId() + " IndexClass :   " + itemIndexclass.getId().getIndexclassId() + " Item Id  " + itemIndexclass.getId().getItemId());
                    CompletableFuture<Itemscount> byId_parentidAndId_parenttypeAndId_classid = null;
                    try {
                        byId_parentidAndId_parenttypeAndId_classid = itemsCountService.
                                findById_parentidAndId_parenttypeAndId_classid(
                                        itemModelParent.getItemid(),
                                        itemModelParent.getType().longValue(),
                                        item.getIndexclass());
                    } catch (InterruptedException e) {
                        // increment error code
                        actionListener.add(JupiterException.Items_Count_Select_EXCEPTION , infoFile);
                        log.error(threadId, "Error  select ItemCount Table database : InterruptedException Exception", e);
                        // change Db , Excel Status
                        changeErrorStatus(infoFile);
                        log.debug(threadId, "start ItemCount rollback process");
                        // rollback
                        rollBackJupiter4.rollBack(items,
                                accessRuleInserted.getId(),
                                linkSaved.getId(),
                                itemIndexclass.getId(),
                                null);
                        log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                        throw new JupiterException("Error  select ItemCount Table Database :  " + e.getMessage(), JupiterException.Interrupted_Exception);
                    }
                    // check If Exists
                    Itemscount itemscountChecked = null;
                    try {
                        itemscountChecked = byId_parentidAndId_parenttypeAndId_classid.get();
                    } catch (InterruptedException e) {
                        // increment error code
                        actionListener.add(JupiterException.ItemIndexClass_Create_EXCEPTION , infoFile);
                        log.error(threadId, "Error  select ItemCount Table database : InterruptedException Exception  ");
                        // change Db , Excel Status
                        changeErrorStatus(infoFile);
                        log.debug(threadId, "start Item ItemCount rollback process");
                        // rollback
                        rollBackJupiter4.rollBack(items,
                                accessRuleInserted.getId(),
                                linkSaved.getId(),
                                itemIndexclass.getId(),
                                null);
                        log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                        throw new JupiterException("Error  select ItemCount Table Database :  " + e.getMessage(), JupiterException.Interrupted_Exception);
                    } catch (ExecutionException e) {
                        // increment error code
                        actionListener.add(JupiterException.Items_Count_Select_EXCEPTION , infoFile);
                        log.error(threadId, "Error select ItemCount Table  database : ExecutionException Exception  ");
                        // change Db , Excel Status
                        changeErrorStatus(infoFile);
                        log.debug(threadId, "start Item ItemCount rollback process");
                        // rollback
                        rollBackJupiter4.rollBack(items,
                                accessRuleInserted.getId(),
                                linkSaved.getId(),
                                itemIndexclass.getId(),
                                null);
                        log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                        throw new JupiterException("Error select ItemCount Database : " + e.getMessage(), JupiterException.Execution_Exception);
                    }
                    // exists
                    if (itemscountChecked != null) {
                        log.info(this.threadId, "Update item Count For Item " + (itemscountChecked.getItemscount().intValue() + 1));
                        // Update into itemsCount
                        itemscountChecked.setItemscount(new BigDecimal(itemscountChecked.getItemscount().intValue() + 1));
                        CompletableFuture<Itemscount> itemsCountUpdated = itemsCountService.save(itemscountChecked);
                        Itemscount itemscountUpdated = null;
                        try {
                            itemscountUpdated = itemsCountUpdated.get();
                        } catch (InterruptedException e) {
                            // increment error code
                            actionListener.add(JupiterException.Items_Count_Update_EXCEPTION , infoFile);
                            log.error(threadId, "Error  Update ItemCount Table database : InterruptedException Exception", e);
                            // change Db , Excel Status
                            changeErrorStatus(infoFile);
                            log.debug(threadId, "start Item ItemCount rollback process");
                            // rollback
                            rollBackJupiter4.rollBack(items,
                                    accessRuleInserted.getId(),
                                    linkSaved.getId(),
                                    itemIndexclass.getId(),
                                    null);
                            log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                            throw new JupiterException("Error   Update ItemCount  Table Database :  " + e.getMessage(), JupiterException.Interrupted_Exception);
                        } catch (ExecutionException e) {
                            // increment error code
                            actionListener.add(JupiterException.Items_Count_Update_EXCEPTION , infoFile);
                            log.error(threadId, "Error Update ItemCount Table  database : ExecutionException Exception  ");
                            // change Db , Excel Status
                            changeErrorStatus(infoFile);
                            log.debug(threadId, "start Item ItemCount rollback process");
                            // rollback
                            rollBackJupiter4.rollBack(items,
                                    accessRuleInserted.getId(),
                                    linkSaved.getId(),
                                    itemIndexclass.getId(),
                                    null);
                            log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                            throw new JupiterException("Error Update ItemCount Database : " + e.getMessage(), JupiterException.Execution_Exception);
                        }
                        // update Record Into database
                        if (itemscountUpdated != null) {
                            log.debug(this.threadId, "Done Update item Count For Item " + (itemscountUpdated.getItemscount().intValue() + 1));
                            // Done insert Item that will returned
                            itemInserted = items;
                            log.debug(this.threadId, "Done Insert All Item Functions Id : " + itemInserted.getItemid());
                        } else {
                            // increment error code
                            actionListener.add(JupiterException.Items_Count_Update_EXCEPTION , infoFile);
                            log.error(threadId, "Error Update Itemscount Into Database");
                            // change Db , Excel Status
                            changeErrorStatus(infoFile);
                            log.debug(threadId, "start Item ItemCount rollback process");
                            // rollback
                            rollBackJupiter4.rollBack(items,
                                    accessRuleInserted.getId(),
                                    linkSaved.getId(),
                                    itemIndexclass.getId(),
                                    null);
                            JupiterException e = new JupiterException("Error insert Itemscount Into Database -- >", JupiterException.Items_Count_Update_EXCEPTION);
                            log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                            throw e;
                        }
                    } else {
                        // if item count not exist Insert into database Row
                        // insert into itemsCountPk
                        ItemscountPK itemscountPK = new ItemscountPK(
                                itemModelParent.getItemid(), //parentid
                                itemModelParent.getType().longValue(), // parenttype
                                item.getIndexclass() //classid
                        );
                        // insert into database itemCount
                        Itemscount itemscount = new Itemscount(itemscountPK, new BigDecimal(1));
                        CompletableFuture<Itemscount> itemscountCompletableFuture = itemsCountService.saveAndFlush(itemscount);
                        // get value
                        Itemscount itemscount1 = null;
                        try {
                            itemscount1 = itemscountCompletableFuture.get();
                        } catch (InterruptedException e) {
                            // increment error code
                            actionListener.add(JupiterException.Items_Count_Insert_EXCEPTION , infoFile);
                            log.error(threadId, "Error  Insert ItemCount Insert database : InterruptedException Exception  ");
                            // change Db , Excel Status
                            changeErrorStatus(infoFile);
                            log.debug(threadId, "start Item ItemCount rollback process");
                            // rollback
                            rollBackJupiter4.rollBack(items,
                                    accessRuleInserted.getId(),
                                    linkSaved.getId(),
                                    itemIndexclass.getId(),
                                    null);
                            log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                            throw new JupiterException("Error   Insert ItemCount  Table Database :  " + e.getMessage(), JupiterException.Interrupted_Exception);
                        } catch (ExecutionException e) {
                            // increment error code
                            actionListener.add(JupiterException.Items_Count_Insert_EXCEPTION , infoFile);
                            log.error(threadId, "Error  Insert ItemCount Insert database : ExecutionException Exception  ");
                            // change Db , Excel Status
                            changeErrorStatus(infoFile);
                            log.debug(threadId, "start Item ItemCount rollback process");
                            // rollback
                            rollBackJupiter4.rollBack(items,
                                    accessRuleInserted.getId(),
                                    linkSaved.getId(),
                                    itemIndexclass.getId(),
                                    null);
                            log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                            throw new JupiterException("Error   Insert ItemCount  Table Database :  " + e.getMessage(), JupiterException.Execution_Exception);
                        }
                        if (itemscount1 != null) {
                            // Done insert Item that will returned
                            itemInserted = items;
                            log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, null);
                        } else {
                            // increment error code
                            actionListener.add(JupiterException.Items_Count_Update_EXCEPTION , infoFile);
                            log.error(threadId, "Error insert Itemscount Into Database -- >");
                            // change Db , Excel Status
                            changeErrorStatus(infoFile);
                            log.debug(threadId, "start Item ItemCount rollback process");
                            // rollback
                            rollBackJupiter4.rollBack(items,
                                    accessRuleInserted.getId(),
                                    linkSaved.getId(),
                                    itemIndexclass.getId(),
                                    null);
                            JupiterException e = new JupiterException("Error insert Itemscount Into Database -- >  ", JupiterException.Items_Count_Update_EXCEPTION);
                            log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                            throw e;
                        }
                    }
                } else {
                    // increment error code
                    actionListener.add(JupiterException.ItemIndexClass_Create_EXCEPTION , infoFile);
                    log.error(threadId, "Error insert ItemIndexclass Into Database -- >");
                    // change Db , Excel Status
                    changeErrorStatus(infoFile);
                    log.debug(threadId, "start Item ItemIndexclass rollback process");
                    // rollback
                    rollBackJupiter4.rollBack(items,
                            accessRuleInserted.getId(),
                            linkSaved.getId(),
                            null,
                            null);
                    JupiterException e = new JupiterException("Error insert ItemIndexclass Into Database -- >", JupiterException.ItemIndexClass_Create_EXCEPTION);
                    log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                    throw e;
                }
            } else {
                // increment error code
                actionListener.add(JupiterException.Links_Create_EXCEPTION , infoFile);
                log.error(threadId, "Error insert Link Into Database -- >");
                // change Db , Excel Status
                changeErrorStatus(infoFile);
                log.debug(threadId, "start Item ItemIndexclass rollback process");
                // rollback
                rollBackJupiter4.rollBack(items,
                        accessRuleInserted.getId(),
                        null,
                        null,
                        null);
                JupiterException e = new JupiterException("Error insert Link Into Database -- > ", JupiterException.Links_Create_EXCEPTION);
                log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
                throw e;
            }
        } else {
            // increment error code
            actionListener.add(JupiterException.Items_Create_EXCEPTION , infoFile);
            log.error(threadId, "Error insert Item Into Database -- >");
            JupiterException e = new JupiterException("Error insert Item Into Database -- > ", JupiterException.Items_Create_EXCEPTION);
            log.LogFunctionExecution(threadId, "createNewItem", System.currentTimeMillis() - startTime, e);
            throw e;
        }
        return itemInserted;
    }

    // method to create access Rule to item
    public Accessrule createAccessRule(Item items , InfoFile infoFile) throws ExecutionException, InterruptedException, JupiterException {
        // create read and write access to item
        //AccessrulePK model
        // ugId = 1 & ugType=1 & acessRight=64
        long startTime = System.currentTimeMillis();
        AccessrulePK accessrulePK = new AccessrulePK(
                items.getItemid(),
                items.getType().longValue(),
                1,
                1
        );
        // access rule model
        Accessrule accessrule = new Accessrule(accessrulePK, new BigDecimal(64));
        // insert into database
        CompletableFuture<Accessrule> accessruleCompletableFuture = accessRuleService.saveAndFlush(accessrule);
        // get isnerted Row
        Accessrule accessruleInserted = accessruleCompletableFuture.get();
        if (accessruleInserted != null) {
            Log.info(this.threadId, "Done create First  Access Rules For Item " + accessruleInserted.getAccessright());
            // ugId = 3 & ugType=2 & acessRight=30
            AccessrulePK accessrulePKTwo = new AccessrulePK(
                    items.getItemid(),
                    items.getType().longValue(),
                    3,
                    2
            );
            // access rule model
            Accessrule accessruleTwo = new Accessrule(accessrulePKTwo, new BigDecimal(30));
            // insert into database
            CompletableFuture<Accessrule> accessruleCompletableFutureTwo = accessRuleService.saveAndFlush(accessruleTwo);
            Accessrule accessruleTwoInserted = accessruleCompletableFutureTwo.get();
            if (accessruleTwoInserted != null) {
                log.info(this.threadId, "Done create Second  Access Rules For Item " + accessruleTwoInserted.getAccessright());
                // done inserted Into Database
                // return true when insert two Times
                log.LogFunctionExecution(threadId, "createAccessRule", System.currentTimeMillis() - startTime, null);
                return accessruleTwoInserted;
            } else {
                // increment error code
                actionListener.add(JupiterException.Access_Rule_Create_EXCEPTION , infoFile );
                // log & throw Exception
                log.error(threadId, "Error Insert First  Access Rule Into database");
                JupiterException e = new JupiterException("Error Insert First  Access Rule Into database ", JupiterException.Access_Rule_Create_EXCEPTION);
                log.LogFunctionExecution(threadId, "createAccessRule", System.currentTimeMillis() - startTime, e);
                throw e;
            }
        } else {
            // increment error code
            actionListener.add(JupiterException.Access_Rule_Create_EXCEPTION , infoFile);
            // log & throw Exception
            log.error(threadId, "Error Insert Second Access Rule Into database");
            JupiterException e = new JupiterException("Error Insert Second Access Rule Into database ", JupiterException.Access_Rule_Create_EXCEPTION);
            log.LogFunctionExecution(threadId, "createAccessRule", System.currentTimeMillis() - startTime, e);
            throw e;
        }
    }

    /* 1 - check knowledgepool
     * 2 - check indexClass for each Folder
     * 3 - get list of links by indexclassId and ParentId to get all Item
     * 4 - get items by label and indexClass
     * 5 - if item > 1 throw exception
     * 6 - check if itemList in links or Not if exists This is A Next parent Id
     * 7 - return Final parentId
     * */
    String searchModel(JupiterSearchModel jupiterSearchModel, InfoFile infoFile) throws JupiterException {
        long startTime = System.currentTimeMillis();
        log.debug(this.threadId, "Start Search Model For Json File ");
        // execute search Model For each folder in jupiter databse
        // knowledge Name
        String knowledgePoolName = jupiterSearchModel.getKnowledgePool().getName();
        log.debug(this.threadId, "KnowledgePool For Json Is : " + knowledgePoolName);
        // folder paths
        LinkedHashMap<Integer, FolderPath> foldersPath = jupiterSearchModel.getFoldersPath();
        if (!knowledgePoolName.isEmpty()) {
            // continue
            try {
                // check knowledgename exists Or Not
                CompletableFuture<Knowledgepool> knowledgepoolServiceByName = knowledgepoolService.findByName(knowledgePoolName);
                Knowledgepool knowledgepool = knowledgepoolServiceByName.get();
                if (knowledgepool != null) {
                    log.debug(this.threadId, "KnowledgePool Is Exists");
                    // count for get step count to init parent id
                    AtomicInteger count = new AtomicInteger();
                    // atom value to parent Id
                    AtomicReference<String> parentId = new AtomicReference<>();
                    // for each FolderPath
                    foldersPath.keySet().forEach(foldersPathKey -> {
                        try {
                            log.info(this.threadId, "foldersPathKey : " + foldersPath);
                            FolderPath folderPath = foldersPath.get(foldersPathKey);
                            // if count = 0 this is knowledgepool
                            if (count.get() == 0) {
                                log.debug(this.threadId, "KnowledgePool ID: " + knowledgepool.getPoolid());
                                log.info(this.threadId, "Jupiter Path caontain only KP. ParantId is KPID: " + knowledgepool.getPoolid());
                                parentId.set(knowledgepool.getPoolid());
                            }
                            // check If IndexClass Found Or Not
                            // if found
                            if (folderPath.getIndexclass() != null) {
                                // seaerch about indexclass folder for each Folder
                                CompletableFuture<Indexclass> indexclassServiceByClassname = indexclassService.findByClassname(folderPath.getIndexclass());
                                Indexclass indexclass = indexclassServiceByClassname.get();
                                if (indexclass != null) {
                                    log.debug(this.threadId, "indexclass ID : " + indexclass.getClassid());
                                    CompletableFuture<List<Link>> byParentidAndiAndLinks = linksService.findByParentidAndiAndLinks(parentId.get(), indexclass.getClassid());
                                    // all links that found parent Id , index class
                                    List<Link> links = byParentidAndiAndLinks.get();
                                    // get specific itemId
                                    CompletableFuture<List<Item>> byLabelAndIndexclass = itemsService.findByLabelAndIndexclass(folderPath.getLabel(), indexclass.getClassid());
                                    List<Item> itemsList = byLabelAndIndexclass.get();
                                    // if there are many itemlist has label , indexClass
                                    if (itemsList.size() != 1) {
                                        // increment error code
                                        log.error(threadId, "Error Search Process, more than one items have same label field and same index-class count: " + itemsList.size());
                                        actionListener.add(JupiterException.Multi_Parent_EXCEPTION , infoFile);
                                        try {
                                            throw new JupiterException("Error Search Process  , Error there are many itemlist has label , indexClass" + itemsList, JupiterException.Multi_Parent_EXCEPTION);
                                        } catch (JupiterException e) {
                                            log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e);
                                            e.printStackTrace();
                                        }
                                        // call Method To some process if path error
                                        errorJupiterPath(infoFile);
                                        parentId.set(null);
                                        return;
                                    } else {
                                        // check if itemList in links or Not
                                        // if exists This is A Next parent Id
                                        boolean isEmptyItem = links.stream().filter(links1 -> {
                                            if (links1.getId().getItemid().equalsIgnoreCase(itemsList.get(0).getItemid())) {
                                                return true;
                                            }
                                            return false;
                                        }).collect(Collectors.toList()).isEmpty();
                                        // if not exist itemlist in links
                                        if (isEmptyItem) {
                                            // increment error code
                                            actionListener.add(JupiterException.Search_Model_EXCEPTION , infoFile);
                                            // throw exception
                                            log.error(threadId, "Error Search Process, error not exist itemlist in LINKS table");
                                            try {
                                                throw new JupiterException("Error Search Process , Error not exist itemlist in links   -- > ", JupiterException.Search_Model_EXCEPTION);
                                            } catch (JupiterException e) {
                                                log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e);
                                                e.printStackTrace();
                                            }
                                            // call Method To some process if path error
                                            errorJupiterPath(infoFile);
                                            parentId.set(null);
                                            return;
                                        } // if exist set parent = item id $ increase count
                                        else {
                                            parentId.set(itemsList.get(0).getItemid());
                                            count.getAndIncrement();
                                            indexclassParent = indexclass;
                                            itemModelParent = itemsList.get(0);
                                            log.debug(this.threadId, "Done Search Process: parentId: " + itemsList.get(0).getItemid() + " IndexClass Is: " + indexclassParent.getClassid() + " Count is: " + count);
                                        }
                                    }
                                } else {
                                    // increment error code
                                    actionListener.add(JupiterException.IndexClassName_Exception , infoFile);
                                    log.error(threadId, "Error Json File , IndexClsss Not Exsists in jupiter database");
                                    try {
                                        throw new JupiterException("Error Json File , IndexClsss Not Exsists into db  -- > ", JupiterException.IndexClassName_Exception);
                                    } catch (JupiterException e) {
                                        log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e);
                                        e.printStackTrace();
                                    }
                                    // call Method To some process if path error
                                    errorJupiterPath(infoFile);
                                    parentId.set(null);
                                    return;
                                }
                            } // if indexClass And metadata not Found
                            else {
                                // new Case
                                // if count = 0 this is knowledgepool
                                if (count.get() == 0) {
                                    log.info(this.threadId, "KnowledgePool ID is: " + knowledgepool.getPoolid());
                                    parentId.set(knowledgepool.getPoolid());
                                }
                                CompletableFuture<List<Item>> itemsServiceByLabel = itemsService.findByLabel(folderPath.getLabel());
                                List<Item> items = itemsServiceByLabel.get();
                                // there is multi Item Or NOt Found Item With This Label
                                if (items.size() != 1) {
                                    // multi Items exists
                                    if (items.size() > 1) {
                                        Map<Item, String> parentIds = new HashMap();
                                        // for each Item exists
                                        // get parent Ids
                                        for (Item item : items) {
                                            // get parent Id
                                            CompletableFuture<Link> byId_itemid = linksService.findById_Itemid(item.getItemid());
                                            // all links that found parent Id , index class
                                            Link link = byId_itemid.get();
                                            // if equal last parent Id
                                            if (link.getId().getParentid().equals(parentId.get())) {
                                                parentIds.put(item, link.getId().getParentid());
                                            }
                                        }
                                        // check List Length
                                        // if 0 not found Item IN this parent
                                        if (parentIds.size() == 0) {
                                            // increment error code
                                            actionListener.add(JupiterException.ITEM_NOT_FOUND_INPARENT_EXCEPTION , infoFile);
                                            log.error(threadId, "Error Search Process, Error item Not Found In This parent  , indexClass " + items);
                                            try {
                                                throw new JupiterException("Error Search Process  , Error item Not Found In This parent , indexClass   -- >" + items, JupiterException.ITEM_NOT_FOUND_INPARENT_EXCEPTION);
                                            } catch (JupiterException e) {
                                                log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e);
                                                e.printStackTrace();
                                            }
                                            // call Method To some process if path error
                                            errorJupiterPath(infoFile);
                                            parentId.set(null);
                                            return;
                                        }
                                        // if > 1 multi items has parent
                                        if (parentIds.size() > 1) {
                                            // increment error code
                                            actionListener.add(JupiterException.Multi_ITEM_INParent_EXCEPTION , infoFile);
                                            log.error(threadId, "Error Search Process, Error there are many item has label in this parent , indexClass" + items);
                                            try {
                                                throw new JupiterException("Error Search Process  , Error there are many item has label  in this parent, indexClass" + items, JupiterException.Multi_ITEM_INParent_EXCEPTION);
                                            } catch (JupiterException e) {
                                                log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e);
                                                e.printStackTrace();
                                            }
                                            // call Method To some process if path error
                                            errorJupiterPath(infoFile);
                                            parentId.set(null);
                                            return;
                                        } // if parent Ids = 1 .. correct
                                        else {
                                            // get item from map
                                            Item item = parentIds.keySet().stream().findFirst().get();
                                            // get indexClass For item Parent
                                            CompletableFuture<Indexclass> byClassid = indexclassService.findByClassid(item.getIndexclass());
                                            parentId.set(item.getItemid());
                                            count.getAndIncrement();
                                            indexclassParent = byClassid.get();
                                            itemModelParent = item;
                                            log.debug(this.threadId, "Done Search Process: parentId: " + item.getItemid() + " IndexClass Is: " + indexclassParent.getClassid() + " Count is: " + count);
                                            log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, null);
                                        }
                                    } else {
                                        // increment error code
                                        actionListener.add(JupiterException.ITEM_NOT_FOUND_INJUPITER_EXCEPTION , infoFile);
                                        log.error(threadId, "Error Search Process  , item not found in jupiter" + items);
                                        try {
                                            throw new JupiterException("Error Search Process, item not found in jupiter, indexClass" + items, JupiterException.ITEM_NOT_FOUND_INJUPITER_EXCEPTION);
                                        } catch (JupiterException e) {
                                            log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e);
                                            e.printStackTrace();
                                        }
                                        // call Method To some process if path error
                                        errorJupiterPath(infoFile);
                                        parentId.set(null);
                                        return;
                                    }
                                } // exists 1 item And unique label
                                else {
                                    // get parent Id
                                    CompletableFuture<Link> byId_itemid = linksService.findById_Itemid(items.get(0).getItemid());
                                    // all links that found parent Id , index class
                                    Link link = byId_itemid.get();
                                    // if equal last parent Id correct
                                    if (link.getId().getParentid().equals(parentId.get())) {
                                        // get indexClass For item Parent
                                        CompletableFuture<Indexclass> byClassid = indexclassService.findByClassid(items.get(0).getIndexclass());
                                        parentId.set(items.get(0).getItemid());
                                        count.getAndIncrement();
                                        indexclassParent = byClassid.get();
                                        itemModelParent = items.get(0);
                                        log.debug(this.threadId, "Done Search Process : parentId: " + items.get(0).getItemid() + " IndexClass Is: " + indexclassParent.getClassid() + " Count is: " + count);
                                    } // if not error ...
                                    else {
                                        // increment error code
                                        actionListener.add(JupiterException.Multi_ITEM_INParent_EXCEPTION , infoFile);
                                        log.error(threadId, "Error Search Process, Error item Not Found In This parent, indexClass" + items);
                                        try {
                                            throw new JupiterException("Error Search Process  , Error item Not Found In This parent , indexClass   -- >" + items, JupiterException.Multi_ITEM_INParent_EXCEPTION);
                                        } catch (JupiterException e) {
                                            log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e);
                                            e.printStackTrace();
                                        }
                                        // call Method To some process if path error
                                        errorJupiterPath(infoFile);
                                        parentId.set(null);
                                        return;
                                    }
                                }
                            }
                        } catch (InterruptedException e) {
                            // increment error code
                            actionListener.add(JupiterException.Interrupted_Exception , infoFile);
                            log.error(threadId, "Error in search : InterruptedException" + e.getMessage());
                            try {
                                throw new JupiterException(e.getMessage(), JupiterException.Interrupted_Exception);
                            } catch (JupiterException e1) {
                                log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e1);
                                e1.printStackTrace();
                                return;
                            }
                        } catch (ExecutionException e) {
                            // increment error code
                            actionListener.add(JupiterException.Execution_Exception , infoFile);
                            log.error(threadId, "Error in search : ExecutionException " + e.getMessage());
                            try {
                                throw new JupiterException(e.getMessage(), JupiterException.Execution_Exception);
                            } catch (JupiterException e1) {
                                log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e1);
                                e1.printStackTrace();
                                return;
                            }
                        }
                    });
                    log.info(this.threadId, "End Search Model ");
                    log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, null);
                    return parentId.get();
                } else {
                    // increment error code
                    actionListener.add(JupiterException.KnowledgePool_Exception , infoFile);
                    log.error(threadId, "Error Json File , knowledgePool Not Exsists in jupiter daatabase");
                    JupiterException e = new JupiterException("Error Json File , knowledgePool Not Exsists into db", JupiterException.KnowledgePool_Exception);
                    log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e);
                    throw e;
                }
            } catch (InterruptedException e) {
                // increment error code
                actionListener.add(JupiterException.Interrupted_Exception , infoFile);
                Log.error(threadId, "InterruptedException  " + e.getMessage());
                log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e);
                throw new JupiterException(e.getMessage(), JupiterException.Interrupted_Exception);
            } catch (ExecutionException e) {
                // increment error code
                actionListener.add(JupiterException.Execution_Exception , infoFile);
                log.error(threadId, "ExecutionException " + e.getMessage());
                log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e);
                throw new JupiterException(e.getMessage(), JupiterException.Execution_Exception);
            }
        } else {
            // increment error code
            actionListener.add(JupiterException.NO_FOLDERS_Exception , infoFile);
            log.error(threadId, "Error Json File , knowledgePoolName is Empty.");
            JupiterException e = new JupiterException("Error Json File , knowledgePoolName is Empty", JupiterException.NO_FOLDERS_Exception);
            log.LogFunctionExecution(threadId, "searchModel", System.currentTimeMillis() - startTime, e);
            throw e;
        }
    }

    // create new Media Server
    public Object[] createNewMedia(Item item, String type, String fullPath, Item parentItem, InfoFile infoFile) throws JupiterException {
        long startTime = System.currentTimeMillis();
        log.info(this.threadId, "start Create Media For Item ");
        Object[] oReturn = new Object[3];
        // if inserted Done
        boolean b = false;
        long currentDate = dateToLong(new Date());
        Mediaserver mediaserver = null;
        // get mediaServer Id to insert into 2 locations create jup file  , ftr schedual
        CompletableFuture<Mediaserver> byMediaserverid = mediaServerService.findByMediaserverid(item.getMediaserverid());
        try {
            mediaserver = byMediaserverid.get();
            log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, null);
        } catch (InterruptedException e) {
            // increment error code
            actionListener.add(JupiterException.Interrupted_Exception , infoFile);
            log.error(threadId, "InterruptedException when get media server by id", e);
            log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, e);
            throw new JupiterException(e.getMessage(), JupiterException.Interrupted_Exception);
        } catch (ExecutionException e) {
            // increment error code
            actionListener.add(JupiterException.Execution_Exception , infoFile);
            Log.error(threadId, "ExecutionException when get media server by id", e);
            log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, e);
            throw new JupiterException(e.getMessage(), JupiterException.Execution_Exception);
        }
        // if mediaserver == null
        if (mediaserver == null) {
            // inc exceptionCode Counter
            actionListener.add(JupiterException.MediaServer_NotFound_Exception , infoFile);
            log.error(threadId, "No Media server Found at media serverid is : " + item.getMediaserverid());
            JupiterException e = new JupiterException("No Media server Found On create Media  ", JupiterException.MediaServer_NotFound_Exception);
            log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, e);
            throw e;
        }
        String locationId = "";
        // if doc
        Mediamanager mediamanagerInserted = null;
        MediamanagerPK mediamanagerPK = null;
        // if document create jup File
        if (item.getType().toString().equalsIgnoreCase(ItemType.DOCUMENT.getItemType())) {
            log.info(this.threadId, "Item Is Document And begin Start create Media For document ");
            try {
                locationId = getLocationId();
            } catch (ExecutionException e) {
                // increment error code
                actionListener.add(JupiterException.Execution_Exception , infoFile);
                log.error(threadId, "Error get LoctionId From sequence", e);
                log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, e);
                throw new JupiterException("Error get LoctionId From sequence on Create Media   " + e.getMessage(), JupiterException.Execution_Exception);

            } catch (InterruptedException e) {
                // increment error code
                actionListener.add(JupiterException.Interrupted_Exception , infoFile);
                log.error(threadId, "Error get LoctionId From sequence", e);
                log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, e);
                throw new JupiterException("Error get LoctionId From sequence on Create Media   " + e.getMessage(), JupiterException.Interrupted_Exception);
            }
            // 1 -  physical Path --> jupiterPath
            // 2 -  new File --> path File in disk
//            jupFile.createFiles("D:\\" + mediaserver.getAddress() + "\\", fileManager.readBytesFromFile(
//                    new File(fullPath)),
//                    locationId,
//                    item.getExt());
            // create mediaManager Id
            mediamanagerPK = new MediamanagerPK(item.getItemid(),
                    Long.parseLong(item.getType().toString())
            );
            // create MediaManager
            Mediamanager mediamanager = new Mediamanager(
                    mediamanagerPK,
                    String.valueOf(currentDate), //creationDate
                    item.getDocumentsize(), //documentsize
                    item.getExt(), // ext
                    "0", // iscompressed
                    "0", // isencrypted
                    locationId, // locationid
                    item.getMultiPageCount() //multiPageCount
            );
            // insert into mediamanager database
            CompletableFuture<Mediamanager> mediamanagerCompletableFuture = mediamanagerService.saveAndFlush(mediamanager);
            try {
                mediamanagerInserted = mediamanagerCompletableFuture.get();
            } catch (InterruptedException e) {
                // increment error code
                actionListener.add(JupiterException.Interrupted_Exception , infoFile);
                log.error(threadId, "Error Create MediaManager  ", e);
                log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, e);
                throw new JupiterException("Error Create MediaManager on Create Media  InterruptedException " + e.getMessage(), JupiterException.Interrupted_Exception);

            } catch (ExecutionException e) {
                // increment error code
                actionListener.add(JupiterException.Execution_Exception , infoFile);
                e.printStackTrace();
                log.error(threadId, "Error Create MediaManager", e);
                log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, e);
                throw new JupiterException("Error Create MediaManager on Create Media  ExecutionException " + e.getMessage(), JupiterException.Execution_Exception);
            }
            if (mediamanagerInserted == null) {
                // increment error code
                actionListener.add(JupiterException.MediaManager_Create_Exception , infoFile);
                log.error(threadId, "Error insert Mediamanager into database ");
                JupiterException e = new JupiterException("Error insert Mediamanager into database ", JupiterException.MediaManager_Create_Exception);
                log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, e);
                throw e;
            } else {
                log.debug(this.threadId, "Done Insert into MediaManager " + mediamanagerInserted.getId().toString());
            }
        }
        log.info(this.threadId, "Begin Create FtrSchedual For any item inserted  doc or folder ");
        // doc or folder
        // values not know
        // 1 -indexclassdata
        // 2 - media address
        Ftrscheduler ftrscheduler = new Ftrscheduler("media" + item.getItemid().replaceAll("\\D+", ""), //operationid
                new BigDecimal(currentDate), //creationdate
                "JUP", //ext
                new BigDecimal(1), //ftroperation
                "b95", // ifsschemapassword
                "", // indexclassdata
                item.getLabel(), //info
                locationId, // locationid
                mediaserver.getAddress(), //mediaaddress
                mediaserver.getPassword(), //mediapassword
                item.getMediaserverid(), //mediaserverid
                new BigDecimal(1), //mediaservertype
                mediaserver.getUsername(), // mediausername
                new BigDecimal(currentDate), // moddate
                item.getItemid(), //objectid
                new BigDecimal(2), //objecttype
                new BigDecimal(0) // status
        );
        // insert into database
        CompletableFuture<Ftrscheduler> ftrschedulerCompletableFuture = ftrschedulerService.saveAndFlush(ftrscheduler);
//        try {
        Ftrscheduler ftrscheduler1 = null;
        try {
            ftrscheduler1 = ftrschedulerCompletableFuture.get();
        } catch (InterruptedException e) {
            // increment error code
            actionListener.add(JupiterException.Interrupted_Exception , infoFile);
            log.error(threadId, "Error Create Ftrschedual", e);
            // Update Database Status Or Update
            changeErrorStatus(infoFile);
            log.debug(threadId, "start Ftrschedual rollback process");
            // rollback create ITem
            rollBackJupiter4.rollBack(item, new AccessrulePK(item.getItemid(),
                    item.getType().longValue(),
                    1, 1),
                    new LinkPK(item.getItemid(),
                            parentItem.getItemid(),
                            parentItem.getType().longValue()),
                    new ItemIndexclassPK(item.getItemid(), item.getIndexclass()),
                    new ItemscountPK(parentItem.getItemid(),
                            parentItem.getType().longValue(),
                            item.getIndexclass()));

            // rollback MediaManager If type Doc
            rollBackJupiter4Med.rollBack(item.getType().toString(),
                    mediamanagerPK,
                    null);
            log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, e);
            throw new JupiterException("Error Create Ftrschedual on Create Media   " + e.getMessage(), JupiterException.Interrupted_Exception);
        } catch (ExecutionException e) {
//            // increment error code
            actionListener.add(JupiterException.Execution_Exception , infoFile);
            log.error(threadId, "Error Create MediaManager  ");
            // Update Database Status Or Update
            changeErrorStatus(infoFile);
            log.debug(threadId, "start MediaManager rollback process");
            // rollback create ITem
            rollBackJupiter4.rollBack(item, new AccessrulePK(item.getItemid(),
                    item.getType().longValue(),
                    1, 1),
                    new LinkPK(item.getItemid(),
                            parentItem.getItemid(),
                            parentItem.getType().longValue()),
                    new ItemIndexclassPK(item.getItemid(), item.getIndexclass()),
                    new ItemscountPK(parentItem.getItemid(),
                            parentItem.getType().longValue(),
                            item.getIndexclass()));
            // rollback MediaManager If type Doc
            rollBackJupiter4Med.rollBack(item.getType().toString(),
                    mediamanagerPK,
                    null);
            log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, e);
            throw new JupiterException("Error Create Ftrschedual on Create Media   " + e.getMessage(), JupiterException.Execution_Exception);
        }
        if (ftrscheduler1 != null) {
            log.info(this.threadId, "Done inserted Ftrschedual Into Database");
            // Done inserted Into Database
            b = true;
            oReturn[0] = b;
            oReturn[1] = locationId;
            oReturn[2] = mediaserver;
        } else {
            actionListener.add(JupiterException.Ftrscheduler_Create_Exception , infoFile);
            log.error(threadId, "Error In Insert Ftrschedual into database");
            // Update Database Status Or Update
            changeErrorStatus(infoFile);
            log.debug(threadId, "start Ftrschedual rollback process");
            // rollback create ITem
            rollBackJupiter4.rollBack(item, new AccessrulePK(item.getItemid(),
                    item.getType().longValue(),
                    1, 1),
                    new LinkPK(item.getItemid(),
                            parentItem.getItemid(),
                            parentItem.getType().longValue()),
                    new ItemIndexclassPK(item.getItemid(), item.getIndexclass()),
                    new ItemscountPK(parentItem.getItemid(),
                            parentItem.getType().longValue(),
                            item.getIndexclass()));

            // rollback MediaManager If type Doc
            rollBackJupiter4Med.rollBack(item.getType().toString(),
                    mediamanagerPK,
                    null);
            JupiterException e = new JupiterException("Error In Insert Ftrschedual into database ", JupiterException.Ftrscheduler_Create_Exception);
            log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, e);
            throw e;
        }
        log.LogFunctionExecution(threadId, "createNewMedia", System.currentTimeMillis() - startTime, null);
        return oReturn;
    }

    // check Signature
    /**
     * check pdf Signature
     *
     * @param reader
     * @return
     * @throws IOException
     */
    public boolean isPdfSigned(PdfReader reader) throws IOException {
        log.info(this.threadId, "start Check Signature For pdf doc ");
        boolean isSigned = false;
        // read pdf document
        // get all signed
        AcroFields fields = reader.getAcroFields();
        // check if there signature
        if (!fields.getSignatureNames().isEmpty()) {
            isSigned = true;
        }
        log.debug(this.threadId, "Item isPdfSigned: " + isSigned);
        return isSigned;
    }

    //    @Async("jsonAsyncExecutor")
    public void updateDocument(JsonUpdateModel jsonUpdateFileModel) {
        // Update document Business
        log.info(this.threadId, "Looking up " + "updateDocument" + "  Thread : " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            // increment error code
           // actionListener.add(JupiterException.Interrupted_Exception , );
            e.printStackTrace();
            new JupiterException(e.getMessage(), JupiterException.Interrupted_Exception);
        }
    }

    //  ---------------- get location Id --------------
    public String getLocationId() throws ExecutionException, InterruptedException {
        // get nextId From sequence
        CompletableFuture<String> lastItem = mediamanagerService.findLastItem();
        String nextId = lastItem.get();
        int zerosLength = Defines.MEDIAMANAGER_LOCATIONID_FIELD_LENGTH - (Defines.DB_JUPITER_MEDIA.length() + nextId.length());
        String locationId = Defines.DB_JUPITER_MEDIA;
        for (int i = 1; i <= zerosLength; i++) {
            locationId += 0;
        }
        locationId += nextId;
        return locationId;
    }

    // -------------- Date -------------------
    /**
     * Insert the method's description here. Creation date: (2000-Mar-00
     * 11:09:38 AM)
     *
     * @param date java.util.Date
     * @return long
     */
    public static long dateToLong(Date date) {
        String dayFormula = null;
        String monthFormula = null;
        String hourFormula = null;
        String minuteFormula = null;
        String secondFormula = null;
        if (date.getHours() < 10) {
            hourFormula = ("0" + date.getHours());
        } else {
            hourFormula = Integer.toString(date.getHours());
        }
        if (date.getMinutes() < 10) {
            minuteFormula = ("0" + date.getMinutes());
        } else {
            minuteFormula = Integer.toString(date.getMinutes());
        }
        if (date.getSeconds() < 10) {
            secondFormula = ("0" + date.getSeconds());
        } else {
            secondFormula = Integer.toString(date.getSeconds());
        }
        if (date.getDate() < 10) {
            dayFormula = ("0" + date.getDate());
        } else {
            dayFormula = Integer.toString(date.getDate());
        }
        if (date.getMonth() < 9) {
            monthFormula = ("0" + (date.getMonth() + 1));
        } else {
            monthFormula = Integer.toString(date.getMonth() + 1);
        }
        String returnedFormula
                = (Integer.toString(1900 + date.getYear()) + monthFormula + dayFormula + hourFormula + minuteFormula
                + secondFormula);
        dayFormula = null;
        monthFormula = null;
        hourFormula = null;
        minuteFormula = null;
        secondFormula = null;
        return Long.parseLong(returnedFormula);
    }

    // change Database Status
    public void changeErrorStatus(InfoFile infoFile) {
        log.debug(this.threadId, "change infofile status in DB to finished and contain errors");
        switch (config.getAutomaticMode()) {
            case Defines.auto:
                // update Database Contains Error
                infoFile.setStatus(Defines.finished);
                infoFile.setFinishStatus(Defines.containErrors);
                infoFile.setFinishDate(new Timestamp(new Date().getTime()));
                infoFileService.updateInfoFile(infoFile);
                log.debug(this.threadId, "done change infofile status in DB");
                break;
            case Defines.excel:
                // update Excel COntains Error
                break;
            default:
                break;
        }
    }

    public void errorJupiterPath(InfoFile infoFile) {
        // Change JSON file in DB
        // update database that finish
        infoFile.setStatus(Defines.finished);
        infoFile.setFinishStatus(Defines.containErrors);
        // set current Time
        infoFile.setFinishDate(new Timestamp(new Date().getTime()));
        // update Database
        infoFileService.updateInfoFile(infoFile);
        // log
        log.error(threadId, "Error In searchModel  in json: " + infoFile.getPath());
        //  Send Mail
        try {
			log.debug(threadId, "send mail to admin user for Error In searchModel  in json");
            sendMail.prepareAndSendTemplate(new mail("", "", infoFile.getPath().substring(infoFile.getPath().lastIndexOf("\\") + 1, infoFile.getPath().lastIndexOf(".")) +" - > Error Jupiter Path" ,
                            "There are Exceptions Occurs During processing json File " + infoFile.getPath() +
                                    " kindly Check Jupiter Tool Log File for Time : " + new Date()),

                    Defines.errorImage ,true , infoFile.getPath());
        } catch (MessagingException ee) {
            log.error(threadId, "error in sending mail", ee);
            ee.printStackTrace();
        }
        //End Thread
//        Thread.currentThread().interrupt();
//        Thread.currentThread().stop();
        return;
    }

    // error in search IndexClass Folder Or Document
    public void errorIndexClassSearch(InfoFile infoFile, String type) {
        // Change JSON file in DB
        // update database that finish
        infoFile.setStatus(Defines.finished);
        infoFile.setFinishStatus(Defines.containErrors);
        // set current Time
        infoFile.setFinishDate(new Timestamp(new Date().getTime()));
        // update Database
        infoFileService.updateInfoFile(infoFile);
        // log
        //log.debug(threadId, "Error In search IndexClass " + type + " in json: " + infoFile.getPath());
        log.error(threadId, "Error search IndexClass " + type + " function in JupiterPath json: " + infoFile.getPath());
        //  Send Mail
        try {
            log.debug(this.threadId, "send mail to admin user");
			sendMail.prepareAndSendTemplate(new mail("", "", infoFile.getPath().substring(infoFile.getPath().lastIndexOf("\\") + 1, infoFile.getPath().lastIndexOf(".")) +" - > Error when Search IndexClass" ,
                            "There are Exceptions Occurs During processing json File " + infoFile.getPath() +
                                    " kindly Check Jupiter Tool Log File for Time : " + new Date()),
                    Defines.errorImage ,true , infoFile.getPath());
        } catch (MessagingException ee) {
            log.error(this.threadId, "error in sending mail", ee);
            ee.printStackTrace();
        }
        //End Thread
//        Thread.currentThread().interrupt();
//        Thread.currentThread().stop();
        return;
    }

    /**
     *
     * @param infoFile
     * @param fullItemPath
     * @return
     */
    public boolean errorDocumentPath(InfoFile infoFile, String fullItemPath) {

        if (fullItemPath != null) {
            // read as a File
            File file = new File(fullItemPath);
            if (file.isDirectory() || !file.canWrite() || !file.canRead()) {
                // change json file
                // log
                // send Mail
                // throw Exception
                // return
                // Change JSON file in DB
                // update database that finish
                infoFile.setStatus(Defines.finished);
                infoFile.setFinishStatus(Defines.containErrors);
                // set current Time
                infoFile.setFinishDate(new Timestamp(new Date().getTime()));
                // update Database
                infoFileService.updateInfoFile(infoFile);
                // log                
                log.error(threadId, "   Error In document path : " + fullItemPath + "  in json  : " + infoFile.getPath());
                //  Send Mail
                try {
                    log.debug(threadId, "send mail to admin user fro error in document path in JSON file");
					sendMail.prepareAndSendTemplate(new mail("", "", infoFile.getPath().substring(infoFile.getPath().lastIndexOf("\\") + 1, infoFile.getPath().lastIndexOf(".")) +" - > Error Document Path",
                                    "There are Exceptions Occurs During processing json File " + infoFile.getPath() +
                                            " kindly Check Jupiter Tool Log File for Time : " + new Date()),
                            Defines.errorImage , true , infoFile.getPath()
                    );
                } catch (MessagingException ee) {
                    ee.printStackTrace();
                }
                //End Thread
//        Thread.currentThread().interrupt();
//        Thread.currentThread().stop();
                return false;
            }
        }
        return true;
    }
    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
