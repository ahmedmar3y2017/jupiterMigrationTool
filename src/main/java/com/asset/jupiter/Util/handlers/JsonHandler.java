/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.handlers;

import com.asset.jupiter.Aop.Unicode;
import com.asset.jupiter.Util.configurationService.config;
import com.asset.jupiter.Util.configurationService.databaseUnicode;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Util.defines.ErrorDefines;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.jsonModel.JsonCreateModel;
import com.asset.jupiter.Util.jsonModel.JsonUpdateModel;
import com.asset.jupiter.Util.jsonModel.createModels.DocumentModel;
import com.asset.jupiter.Util.jsonModel.createModels.FolderModel;
import com.asset.jupiter.Util.jsonModel.createModels.ItemModel;
import com.asset.jupiter.Util.jsonModel.searchModels.FolderPath;
import com.asset.jupiter.Util.jsonModel.searchModels.JupiterSearchModel;
import com.asset.jupiter.Util.jsonModel.searchModels.KnowledgePool;
import com.asset.jupiter.Util.jsonModel.updateModels.DocumentUpdateModel;
import com.asset.jupiter.Util.jsonModel.updateModels.FolderUpdateModel;
import com.asset.jupiter.Util.jsonModel.updateModels.ItemUpdateModel;
import com.asset.jupiter.Util.logging.Log;
import com.asset.jupiter.Util.logging.ThreadLogger;
import com.google.gson.Gson;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONTokener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
@Scope(value = "prototype", proxyMode = ScopedProxyMode.NO)
@Component
public class JsonHandler {

    //    private JsonUpdateModel jsonUpdateModel;
    private int FilesCount = 0;
    private String jsonfilPath;
    private String mode;
    // configuration Check
    @Autowired
    com.asset.jupiter.Util.configurationService.config config;
//    @PostConstruct
//
//    public void init() {
//        System.out.println("Done Read Json");
//    }
    //    private JsonHandler() {
//        super();
//    }
//
//    public JsonHandler(String jsonfilPath) throws JupiterException {
//        super();
//
//        // to read json
//        readJson();
//    }
//    @Async("jsonAsyncExecutor")
    @Unicode
    public Object readJson(String jsonfilPath, String threadId, String jsonName) throws JupiterException {
        long startTime = System.currentTimeMillis();
        ThreadLogger log = new ThreadLogger(threadId, jsonName);
        if (isNullOrEmpty(jsonfilPath)) {
            log.error(threadId, "json file path is null or empty", new JupiterException("json file Path or mode  is Null or empty", JupiterException.NULL_OR_EMPTY_VLAUE));
            throw new JupiterException("json file Path or mode  is Null or empty", JupiterException.NULL_OR_EMPTY_VLAUE);
        }


        setJsonfilPath(jsonfilPath);
        Log.info(threadId, "Start parsing Json file: " + getJsonfilPath() + " *** Mode: " + getMode());
        log.debug(threadId, "Start parsing Json file: " + getJsonfilPath() + " *** Mode: " + getMode());
        Object o = readJsonFile(getJsonfilPath(), threadId, jsonName);
        Gson gson = new Gson();
        if (getMode().equals(Defines.createMode)) {
            //JsonCreateModel jsonCreateFileModel = (JsonCreateModel) o;
            //String jsonCreateStr = gson.toJson(jsonCreateFileModel, JsonCreateModel.class);
            log.logFunctionCalling(threadId, "readJson", System.currentTimeMillis() - startTime, new Object[]{jsonfilPath, threadId, jsonName});
        } else if (getMode().equals(Defines.updateMode)) {
            long startupdate = System.currentTimeMillis();
            //JsonUpdateModel jsonUpdateFileModel = (JsonUpdateModel) o;
            //String jsonUpdateStr = gson.toJson(jsonUpdateFileModel, JsonUpdateModel.class);
            Log.debug(threadId, "End at: " + (System.currentTimeMillis() - startupdate));
            log.logFunctionCalling(threadId, "readJson", System.currentTimeMillis() - startTime, new Object[]{jsonfilPath, threadId, jsonName});
        }
        return o;
    }

    /**
     * function to read JSOn file and parse it to Object
     *
     * @param path
     * @param threadId
     * @param jsonName
     * @return
     */
    public Object readJsonFile(String path, String threadId, String jsonName) {
        long startTime = System.currentTimeMillis();
        ThreadLogger log = new ThreadLogger(threadId, jsonName);
//        try (Reader reader = new FileReader(path);
//             BufferedReader buffered = new BufferedReader(reader)) {
//            // TODO: input
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Object o = null;
//        FileReader fileReader = null ;
        JSONParser parser = new JSONParser();
        try ( FileReader fileReader = new FileReader(path);){
            log.info(threadId, "try loading JSOn file: " + path);
//            new BufferedReader(new InputStreamReader(new FileInputStream("src/qqqqqqqq/json"), "UTF-8"));
//            Object fileObj = parser.parse(new BufferedReader(new InputStreamReader(new FileInputStream(path), "windows-1252")));
            Object fileObj = parser.parse(fileReader);
            JSONObject fileJsonObject = (JSONObject) fileObj;
            try {
                String s = databaseUnicode.encodeToDatabse(fileJsonObject.toJSONString());
                fileJsonObject = (JSONObject) parser.parse(s);
            } catch (SQLException e) {
                log.LogFunctionExecution(threadId, "readJsonFile", System.currentTimeMillis() - startTime, e);
                log.error(threadId, "error in getting database encoding", e);
                e.printStackTrace();
            }
            if (isValidatedJson(fileJsonObject, log)) {
                log.info(threadId, "JSON file is valid");
                String mode = (String) fileJsonObject.get("mode");                
                log.debug(threadId, "JSON file mode is " + mode);
                setMode(mode);
                if (mode.equals(Defines.createMode)) {
                    o = parseJsonCreateMode(fileJsonObject);
                } else if (mode.equals(Defines.updateMode)) {
                    parseJsonUpdateMode(fileJsonObject);
                    o = null;
                } else {
                    log.error(threadId, "JSON file mode is not supported", new Exception("JSON file Mode is not supported, mode must be create or update."));
                    log.LogFunctionExecution(threadId, "readJsonFile", System.currentTimeMillis() - startTime, new JupiterException(ErrorDefines.UNSUPPORTED_JSONFILE_MODE, JupiterException.UNSUPPORTED_JSONFILE_MODE));
                    throw new JupiterException(ErrorDefines.UNSUPPORTED_JSONFILE_MODE, JupiterException.UNSUPPORTED_JSONFILE_MODE);
                }
            } else {
                // move file to failed Folder inside integration Folder
                // move this File to Failed Folder
                Path temp = null;
                try {
                    // integration Path
                    String integrationFolderPath = config.getIntegrationFolderPath();
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
            }
        } catch (FileNotFoundException ex) {
            log.error(threadId, "error JSON file is not found", ex);
        } catch (IOException ex) {
            log.LogFunctionExecution(threadId, "readJsonFile", System.currentTimeMillis() - startTime, ex);
        } catch (ParseException ex) {
            log.error(threadId, "parsing exception while read JSON file", ex);            
            // if throw exception in parser because Json file is null or structure Invalid
            // move to failed Folder
            // move file to failed Folder inside integration Folder
            // move this File to Failed Folder
            Path temp = null;
            try {
                // integration Path
                String integrationFolderPath = config.getIntegrationFolderPath();

                String newFile = integrationFolderPath + "\\Failed\\" + path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf(".")) + "-FAILED.json";
                temp = Files.copy
                        (Paths.get(path),
                                Paths.get(newFile), StandardCopyOption.REPLACE_EXISTING);

//                                    delete This File
                    File file1 = new File(path);
                    if (file1.exists())
                        Files.deleteIfExists(Paths.get(path));
//                                    System.out.println("File deleted: ");
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (temp != null) {
            } else {
            }   
			log.LogFunctionExecution(threadId, "readJsonFile", System.currentTimeMillis() - startTime, ex);			
        } catch (JupiterException ex) {
            log.LogFunctionExecution(threadId, "readJsonFile", System.currentTimeMillis() - startTime, ex);
        }
        return o;
    }

    /**
     * read and parse JSON file in create Mode
     *
     * @param fileJsonObject
     */
    private JsonCreateModel parseJsonCreateMode(JSONObject fileJsonObject) throws JupiterException {
        JsonCreateModel jsonCreateModel = new JsonCreateModel();
        try {
            jsonCreateModel.setMode(Defines.createMode);
            String directory = (String) fileJsonObject.get("documentsPhysicalPath");
            if(isNullOrEmpty(directory)){
                throw new JupiterException("com.asset.jupiter.exception.JsonParseException documentsPhysicalPath not found");
            }
            JSONArray jsonData = (JSONArray) fileJsonObject.get("data");
            if(jsonData == null){
                throw new JupiterException("com.asset.jupiter.exception.JsonParseException data object not found");
            }
            jsonCreateModel.setDocumentsPhysicalPath(directory);
            Iterator data_iterator = jsonData.iterator();
            while (data_iterator.hasNext()) {
                ItemModel itemModel = new ItemModel();
                JupiterSearchModel jupiterSearchModel = new JupiterSearchModel();
                JSONObject itemModel_Obj = (JSONObject) data_iterator.next();
                JSONObject pathObj = (JSONObject) itemModel_Obj.get(Defines.path);
                String jupiterPath = (String) pathObj.get(Defines.JupiterPath);
                // check split may be / or \
                String split = jupiterPath.trim().contains("/") ? "/" : "\\\\";
                String[] pathes = jupiterPath.split(split);
                for (int i = 0; i < pathes.length; i++) {
                    String parent = pathes[i];
                    if (i == 0) {//parent is KP
                        KnowledgePool knowledgePool = new KnowledgePool();
                        knowledgePool.setName(parent);
                        jupiterSearchModel.setKnowledgePool(knowledgePool);
                    } else {//parent is Folder
                        FolderPath folderPath = new FolderPath();
                        folderPath.setLabel(parent);
                        // check If NUll
                        JSONObject folder_metadata = (JSONObject) pathObj.get(parent);
                        // if not NUll
                        if (folder_metadata != null) {

                            String indexclass_Name = (String) folder_metadata.get("indexclass");
                            if(isNullOrEmpty(indexclass_Name)){
                                throw new JupiterException("com.asset.jupiter.exception.JsonParseException indexclass of folder "+parent+"in jupiter path not found");
                            }
                            folderPath.setIndexclass(indexclass_Name);
                            if (folder_metadata.containsKey(indexclass_Name)) {
                                JSONObject indexclass_Metadata = (JSONObject) folder_metadata.get(indexclass_Name);
                                LinkedHashMap<String, String> folderPath_Metadata = new LinkedHashMap<String, String>();
                                for (Iterator iterator = indexclass_Metadata.keySet().iterator(); iterator.hasNext();) {
                                    String key = (String) iterator.next();
                                    String value = (String) indexclass_Metadata.get(key);
                                    folderPath_Metadata.put(key, value);
                                }
                                folderPath.setMetadata(folderPath_Metadata);
                            }
                        }
                        jupiterSearchModel.addFolder(folderPath);
                    }
                }
                itemModel.setJupiterPath(jupiterSearchModel);
                //parsing folders that will be imported
                JSONArray folders_arr = (JSONArray) itemModel_Obj.get("folders");
                if(folders_arr == null){
                    throw new JupiterException("com.asset.jupiter.exception.JsonParseException folders array not found");
                }
                Iterator foldersiterator = folders_arr.iterator();
                while (foldersiterator.hasNext()) {
                    FolderModel folderModel = new FolderModel();
                    JSONObject folderModel_json = (JSONObject) foldersiterator.next();
                    String folder_indexclass = (String) folderModel_json.get("indexclass");
                    if(isNullOrEmpty(folder_indexclass)){
                        throw new JupiterException("com.asset.jupiter.exception.JsonParseException folders indexclass not found");
                    }
                    folderModel.setIndexclass(folder_indexclass);
                    JSONObject folder_metadata = (JSONObject) folderModel_json.get(folder_indexclass);
                    LinkedHashMap<String, String> folderModel_Metadata = new LinkedHashMap<String, String>();
                    for (Iterator iterator = folder_metadata.keySet().iterator(); iterator.hasNext();) {
                        String key = (String) iterator.next();
                        String value = (String) folder_metadata.get(key);
                        folderModel_Metadata.put(key, value);
                    }
                    folderModel.setMetadata(folderModel_Metadata);
                    JSONArray documents_arr = (JSONArray) folderModel_json.get("documents");
                    if(documents_arr == null){
                        throw new JupiterException("com.asset.jupiter.exception.JsonParseException documents array not found under folder "+folder_indexclass);
                    }
                    Iterator documentsiterator = documents_arr.iterator();
                    while (documentsiterator.hasNext()) {
                        DocumentModel document = new DocumentModel();
                        JSONObject document_Obj = (JSONObject) documentsiterator.next();
                        String document_path = (String) document_Obj.get("path");
                        if(isNullOrEmpty(document_path)){
                            throw new JupiterException("com.asset.jupiter.exception.JsonParseException document path not found ");
                        }
                        String document_indexclass = (String) document_Obj.get("indexclass");
                        JSONObject document_metadata = (JSONObject) document_Obj.get(document_indexclass);
                        if(document_metadata == null){
                            throw new JupiterException("com.asset.jupiter.exception.JsonParseException indexclass "+document_indexclass+" metadata not found ");
                        }
                        document.setIndexclass(document_indexclass);
                        document.setPath(document_path);
                        LinkedHashMap<String, String> documentModel_Metadata = new LinkedHashMap<String, String>();
                        for (Iterator iterator = document_metadata.keySet().iterator(); iterator.hasNext();) {
                            String key = (String) iterator.next();
                            String value = (String) document_metadata.get(key);
                            documentModel_Metadata.put(key, value);
                        }
                        document.setMetadata(documentModel_Metadata);
                        folderModel.addDocument(document);
                    }
                    itemModel.addFolder(folderModel);
                }
                jsonCreateModel.addItemModel(itemModel);
            }
        } catch (JupiterException e) {
            e.printStackTrace();
            throw e;
        }catch(Exception e){
            e.printStackTrace();
            throw new JupiterException(e.getMessage());
        }
        return jsonCreateModel;
    }

    /**
     * read and parse JSON file in update mode
     *
     * @param fileJsonObject
     */
    private void parseJsonUpdateMode(JSONObject fileJsonObject) throws JupiterException {
        JsonUpdateModel jsonUpdateModel = new JsonUpdateModel();
        try {
            jsonUpdateModel.setMode(Defines.updateMode);
            String directory = (String) fileJsonObject.get(Defines.directory);
            JSONArray jsonData = (JSONArray) fileJsonObject.get("data");
            jsonUpdateModel.setDocumentsPhysicalPath(directory);
            Iterator data_iterator = jsonData.iterator();
            while (data_iterator.hasNext()) {
                ItemUpdateModel itemUpdateModel = new ItemUpdateModel();
                JupiterSearchModel jupiterSearchModel = new JupiterSearchModel();
                JSONObject itemUpdateModel_Obj = (JSONObject) data_iterator.next();
                JSONObject pathObj = (JSONObject) itemUpdateModel_Obj.get(Defines.path);
                String jupiterPath = (String) pathObj.get(Defines.JupiterPath);
                String[] pathes = jupiterPath.split("/");
                for (int i = 0; i < pathes.length; i++) {
                    String parent = pathes[i];
                    if (i == 0) {//parent is KP
                        KnowledgePool knowledgePool = new KnowledgePool();
                        knowledgePool.setName(parent);
                        jupiterSearchModel.setKnowledgePool(knowledgePool);
                    } else {//parent is Folder
                        FolderPath folderPath = new FolderPath();
                        JSONObject folder_metadata = (JSONObject) pathObj.get(parent);
                        String indexclass_Name = (String) folder_metadata.get(Defines.indexclass);
                        folderPath.setIndexclass(indexclass_Name);
                        folderPath.setLabel(parent);
                        JSONObject indexclass_Metadata = (JSONObject) folder_metadata.get(indexclass_Name);
                        LinkedHashMap<String, String> folderPath_Metadata = new LinkedHashMap<String, String>();
                        for (Iterator iterator = indexclass_Metadata.keySet().iterator(); iterator.hasNext();) {
                            String key = (String) iterator.next();
                            String value = (String) indexclass_Metadata.get(key);
                            folderPath_Metadata.put(key, value);
                        }
                        folderPath.setMetadata(folderPath_Metadata);
                        jupiterSearchModel.addFolder(folderPath);
                    }
                }
                itemUpdateModel.setJupiterPath(jupiterSearchModel);
                //parsing folders and documents that need modifications
                JSONArray modifications_arr = (JSONArray) itemUpdateModel_Obj.get(Defines.modifications);
                Iterator modificationsiterator = modifications_arr.iterator();
                while (modificationsiterator.hasNext()) {
                    JSONObject updateModel_json = (JSONObject) modificationsiterator.next();
                    String type = (String) updateModel_json.get(Defines.type);
                    String indexclass = (String) updateModel_json.get(Defines.indexclass);
                    JSONObject oldMetadata_json = (JSONObject) updateModel_json.get(Defines.oldMetadata);
                    JSONObject newMetadata_json = (JSONObject) updateModel_json.get(Defines.newMetadata);
                    HashMap<String, String> old_Metadata = new HashMap<String, String>();
                    HashMap<String, String> new_Metadata = new HashMap<String, String>();
                    for (Iterator iterator = oldMetadata_json.keySet().iterator(); iterator.hasNext();) {
                        String indexclassname = (String) iterator.next();
                        JSONObject metadata = (JSONObject) oldMetadata_json.get(indexclassname);
                        for (Iterator metadataIterator = metadata.keySet().iterator(); metadataIterator.hasNext();) {
                            String key = (String) metadataIterator.next();
                            String value = (String) metadata.get(key);
                            old_Metadata.put(key, value);
                        }
                    }
                    for (Iterator iterator = newMetadata_json.keySet().iterator(); iterator.hasNext();) {
                        String indexclassname = (String) iterator.next();
                        JSONObject metadata = (JSONObject) newMetadata_json.get(indexclassname);
                        for (Iterator metadataIterator = metadata.keySet().iterator(); metadataIterator.hasNext();) {
                            String key = (String) metadataIterator.next();
                            String value = (String) metadata.get(key);
                            new_Metadata.put(key, value);
                        }
                    }
                    if (type.equals(Defines.folderItem)) {
                        FolderUpdateModel folderUpdateModel = new FolderUpdateModel();
                        folderUpdateModel.setIndexclass(indexclass);
                        folderUpdateModel.setNewMetadata(new_Metadata);
                        folderUpdateModel.setOldMetadata(old_Metadata);
                        itemUpdateModel.addFolderUpdateModel(folderUpdateModel);
                    } else if (type.equals(Defines.documentItem)) {
                        DocumentUpdateModel documentUpdateModel = new DocumentUpdateModel();
                        String path = (String) updateModel_json.get(Defines.path);
                        documentUpdateModel.setPath(path);
                        documentUpdateModel.setIndexclass(indexclass);
                        documentUpdateModel.setOldMetadata(old_Metadata);
                        documentUpdateModel.setNewMetadata(new_Metadata);
                        itemUpdateModel.addDocumentUpdateModel(documentUpdateModel);
                    }
                }
                jsonUpdateModel.addItemUpdateModel(itemUpdateModel);
            }
        } finally {
        }
    }

    /**
     * validate json file content
     *
     * @param fileJsonObject
     * @return true if valid, false if not valid
     */
    private boolean isValidatedJson(JSONObject fileJsonObject, ThreadLogger log) throws IOException, ParseException {
        // get schema json path
//        String pathLogXml = new ClassPathResource("src/main/resources/shema.json").getPath();
//        String pathLogXml = new ClassPathResource("shema.json").getPath();
//        InputStream inputStream = new FileInputStream(pathLogXml);
        long startTime = System.currentTimeMillis();
        log.debug(log.getThreadId(), "validate JSON file: " + log.getLogging_Path());
        InputStream inputStream = new ClassPathResource("shema.json").getInputStream();
        org.json.JSONObject rawSchema = new org.json.JSONObject(new JSONTokener(inputStream));
        SchemaLoader loader = SchemaLoader.builder()
                .schemaJson(rawSchema).draftV7Support() // or draftV7Support()
                .build();
        Schema schema = loader.load().build();
        try {
            String s = fileJsonObject.toJSONString();
            org.json.JSONObject jsonObject = new org.json.JSONObject(s);
            schema.validate(jsonObject);
            log.LogFunctionExecution(log.getThreadId(), "isValidatedJson", System.currentTimeMillis() - startTime,null);
        } catch (ValidationException e) {
            // prints #/rectangle/a: -5.0 is not higher or equal to 0
            log.LogFunctionExecution(log.getThreadId(), "isValidatedJson", System.currentTimeMillis() - startTime, e);
            e.printStackTrace();
            return false;
        } finally {
            inputStream.close();
        }
        return true;
    }

    /**
     * check Null strings
     *
     * @param o
     * @return
     */
    private static boolean isNull(Object o) {
        return o == null;
    }

    /**
     * @return the FilesCount
     */
    public int getFilesCount() {
        return FilesCount;
    }

    /**
     * @return the jsonCreateModel
     */
//    public JsonCreateModel getJsonCreateModel() {
//        return jsonCreateModel;
//    }
    /**
     * @return the jsonUpdateModel
     */
//    public JsonUpdateModel getJsonUpdateModel() {
//        return jsonUpdateModel;
//    }
    /**
     * @return the jsonfilPath
     */
    public String getJsonfilPath() {
        return jsonfilPath;
    }

    /**
     * @param jsonfilPath the jsonfilPath to set
     */
    public void setJsonfilPath(String jsonfilPath) {
        this.jsonfilPath = jsonfilPath;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * check the string is null or empty
     *
     * @param str
     * @return true if null, false if not null
     */
    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
