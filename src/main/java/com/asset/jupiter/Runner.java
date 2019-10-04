package com.asset.jupiter;

import com.asset.jupiter.JUPITER.Model.Entities.InfoFile;
import com.asset.jupiter.JUPITER.jupiterService.*;
import com.asset.jupiter.Util.CrunchifyGetIPHostname;
import com.asset.jupiter.Util.FolderWatcher.folderCreate;
import com.asset.jupiter.Util.FolderWatcher.folderDelete;
import com.asset.jupiter.Util.FolderWatcher.folderUpdate;
import com.asset.jupiter.Util.MailConfig.SendMail;
import com.asset.jupiter.Util.MailConfig.mail;
import com.asset.jupiter.Util.configurationService.config;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Util.excelModel.ExcelModel;
import com.asset.jupiter.Util.excelModel.RowModel;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.handlers.ExcelHandler;
import com.asset.jupiter.Util.handlers.JsonHandler;
import com.asset.jupiter.Util.logging.Log;
import com.asset.jupiter.Util.operations.DocumentHandler;
import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.NO)
public class Runner implements CommandLineRunner {

    @Autowired
    ExcelHandler excelHandler;
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.accesstemplatesService accesstemplatesService;
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.knowledgepoolService knowledgepoolService;
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.indexclassService indexclassService;
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.linksService linksService;
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.itemsService itemsService;
    @Autowired
    tablesService tablesService;
    @Autowired
    infoFileService infoFileService;
    @Autowired
    JsonHandler jsonHandler;
    @Autowired
    DocumentHandler documentHandler;
    @Autowired
    CrunchifyGetIPHostname crunchifyGetIPHostname;
    @Autowired
    Log log;
    @Autowired
    SendMail sendMail;
    // configuration Check
    @Autowired
    config config;
    // folder watcher events
    @Autowired
    folderCreate folderCreate;
    @Autowired
    folderUpdate folderUpdate;
    @Autowired
    folderDelete folderDelete;
    @Autowired
    AppThread appThread;
    List<InfoFile> infoFileServiceAllStatus = null;
    String machineIp = null;

    @Override
    public void run(String... args) {
        // checking the total memeory
//      Runtime gfg = Runtime.getRuntime();
//      System.out.println(databaseUnicode.getUnicode());
        Log.info(Runner.class.getName(), "*******Jupiter Bulk Upload Tool Start******");
        Log.info(Runner.class.getName(), "Time : " + String.valueOf(new Date().getTime()));
// **************************** begin start when app run *******************************************
        // create new instance of machine
        /*
         * 1 - ip address
         * 2 - currentDate
         * 3 - unique Id genaretor
         * */
        String ipAddress = crunchifyGetIPHostname.getIp();
        long currentDate = Calendar.getInstance().getTimeInMillis();
        String uniqueId = crunchifyGetIPHostname.getUniqueId();
        machineIp = ipAddress + Defines.fileSeparation + currentDate + Defines.fileSeparation + uniqueId;
        // send Mail When Start Tool
        try {
            sendMail.prepareAndSendTemplate(new mail("", "", "Jupiter Migration Tool Service", "Kindly be informed that Jupiter Migration Tool has been started successfully on  " + new Date()
                    + " , on the following path: " + machineIp),
                    Defines.startImage , false , ""
            );
        } catch (MessagingException e) {
            Log.error(Runner.class.getName(), "exception during sending application startup mail", e);
            e.printStackTrace();
        }
        Log.info(Runner.class.getName(), "Send Mail : jupiter  Machine Ip : " + machineIp);
        // thread Ip
        String threadWatcherIp = machineIp + Defines.fileSeparation + crunchifyGetIPHostname.getUniqueId();
        // start integrated Folder Watcher
        new Thread(new Runnable() {
            @Override
            public void run() {
                folderWatcher();
            }
        }, threadWatcherIp).start();
//      integratedFolderWatcher();
//      check If automatic Or From Excel Sheet
        switch (config.getAutomaticMode()) {
            case Defines.auto:
                // auto
                executeAuto(machineIp);
                break;
            case Defines.excel:
                // excel
                try {
                    // call excel Sheet
                    executeExcel(machineIp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Log.error(Runner.class.getName(), "error: application mode is not defined correctly. please set it auto or excel");
                break;
        }
    }

    // for Auto Mode
    public void executeAuto(String machineIp) {

        // start Work With database
        // 1 - get all pathes From FolderIntegration
        // 2 - insert into Database INFO_FILE
        // 3 - start Tool With This File Pathes database
        // 1 - get all pathes From FolderIntegration
        Set<String> paths = null;
        try {
            // get only .jsonfile
            paths = Files.walk(Paths.get(config.getIntegrationFolderPath())).
                    filter(path -> {
                        if (path.getFileName().toString().contains(".json")
                                && !path.getFileName().toString().contains("-FAILED")
                                && !path.getFileName().toString().contains("-FINISHED")) {
                            return true;
                        }
                        return false;
                    }).
                    map(Path::toString).
                    collect(Collectors.toSet());
        } catch (IOException e) {
            Log.error("executeAuto", "exception during getting JSON files from the Integration folder", e);
            e.printStackTrace();
        }
        // 2.0 - get difference Only that will inserted into database
        // get all infoFiles
        List<InfoFile> allDatabaseInfoFiles = infoFileService.findAll();
        // get set database  pathes Only
        Set<String> allDatabasePathes = allDatabaseInfoFiles.stream().
                map(InfoFile::getPath).
                collect(Collectors.toSet());
        //get only difference
        paths.removeAll(allDatabasePathes);

        // 2.1 - insert into Database INFO_FILE difference
        paths.stream().forEach(path
                -> {
            // create Model InfoFIles
            InfoFile infoFile = new InfoFile("", "", path.toString(), new Timestamp(new Date().getTime()), null, null, Defines.jsonReady);
            CompletableFuture<InfoFile> infoFileCompletableFuture = infoFileService.saveAndFlush(infoFile);
            try {
                InfoFile infoFileInserted = infoFileCompletableFuture.get();
            } catch (InterruptedException e) {
                Log.error("executeAuto", "InterruptedException exception during saving JSON files pathes in Database.", e);
                e.printStackTrace();
            } catch (ExecutionException e) {
                Log.error("executeAuto", "ExecutionException exception during saving JSON files pathes in Database", e);
                e.printStackTrace();
            }
        });
        // 3 - start Tool With This File Pathes database
        // start Tool Work
        infoFileServiceAllStatus = infoFileService.findAllByStatus(Defines.jsonReady);
        infoFileServiceAllStatus.stream().
                forEach(infoFile -> {
                    // start process
                    startProcess(Defines.auto, infoFile.getPath(), machineIp, 0, infoFile);

                });
//                    sendMail.prepareAndSendTemplate(new mail("", "",
//                            "Kindly be informed that A New Thread has been Started for jsonFile path : "
//                                    + path + " With Thread Id ->  " +
//                                    threadIp + " in Time : " + new Date()) ,
//                            Defines.threadImage);
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                }

    }

    // foe excel Mode
    public void executeExcel(String machineIp) throws IOException {

        // get all in excel cheet
        ExcelModel excelModel = excelHandler.getReadyPathes();
        Log.debug(Runner.class.getName(), "Get Ready Pathes : " + excelModel.getrowlModels());

        List<RowModel> rowModels = excelModel.getrowlModels();
        Log.debug(Runner.class.getName(), "Excel Rows : Size :  " + rowModels.size());
        // check if excel sheet is empty or not
        if (excelModel.getrowlModels().size() <= 0) {

            Log.error(Runner.class.getName(), "Error excel sheet is empty ");
            Log.error(Runner.class.getName(), "Stop Tool Running ");
            new JupiterException("Error excel sheet is empty, Stop Tool Running ");
            // close application
            System.exit(0);
        }
        // for each row and begin thread
        // each row has has a json file and own thread
        rowModels.forEach(rowModel -> {
            startProcess(Defines.excel, rowModel.getPath(), machineIp, rowModel.getRowNum(), null);
        });
    }
//    // to display all beans
//    public static void displayAllBeans() {
//        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
//        for (String beanName : allBeanNames) {
//            System.out.println(beanName);
//        }
//    }
    //start ALl Process

    public void startProcess(String type,
            String path, String machineIp,
            int rowCount, InfoFile infoFile) {
        // thread Ip
        String threadIp = machineIp + Defines.fileSeparation + crunchifyGetIPHostname.getUniqueId();
        // log
        Log.info(threadIp, "Start Thread for Json File : " + path);
        Log.info(threadIp, "Send Mail from jupiter : " + path);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // every 5 threads send Mail
                    // send Mail When Start Thread Json
//                try {
//                    sendMail.prepareAndSendTemplate(new mail("", "",
//                            "Kindly be informed that A New Thread has been Started for jsonFile path : "
//                                    + path + " With Thread Id ->  " +
//                                    threadIp + " in Time : " + new Date()) ,
//                            Defines.threadImage);
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                }

// thread Function
                    appThread.start(type,
                            path,
                            rowCount, infoFile, threadIp);
                } catch (JupiterException ex) {
                    Log.error(threadIp, "error in thread that make it stop processing: " + ex.getMessage(), ex);
                    ex.printStackTrace();
                }
            }
        }, threadIp);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // folder watcher
    public void folderWatcher() {
        try {
            // Creates a instance of WatchService.
            WatchService watcher = FileSystems.getDefault().newWatchService();
            // Registers the logDir below with a watch service.
            Path logDir = Paths.get(config.getIntegrationFolderPath());
            logDir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
            // Monitor the logDir at listen for change notification.
            while (true) {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    String fileName = event.context().toString();
                    String filePath = config.getIntegrationFolderPath() + Defines.separation + fileName;
                    // set affect only .json File
                    if (fileName.contains(".json")) {
                        if (kind == ENTRY_CREATE) {
                            // create Folder Mode
                            Log.info(getClass().getName(), "Entry was created on log dir.:" + event.context() + " :: " + ((Path) event.context()).toAbsolutePath());
                            try {
                                InfoFile fileInfo = folderCreate.fileCreateBusiness(fileName, filePath);
                                // generate new ThreadIp
                                String threadIp = machineIp + Defines.fileSeparation + crunchifyGetIPHostname.getUniqueId();
                                // start New Thread
                                Runnable r = new Runnable() {
                                    public void run() {
                                        try {
                                            // senf Mail When Start
//                                    try {
//                                        sendMail.prepareAndSendTemplate(new mail("", "",
//                                                "Kindly be informed that A New Thread has been Started for jsonFile path : "
//                                                        + fileInfo.getPath() + " With Thread Id ->  " +
//                                                        threadIp + " in Time : " + new Date()) ,
//                                                Defines.threadImage);
//                                    } catch (MessagingException e) {
//                                        e.printStackTrace();
//                                    }
// thread Function
                                            appThread.start(Defines.auto,
                                                    fileInfo.getPath(),
                                                    0, fileInfo, threadIp);
                                        } catch (JupiterException ex) {
                                            Log.error(threadIp, "error in thread that make it stop processing: " + ex.getMessage(), ex);
                                            ex.printStackTrace();
                                        }
                                    }
                                };
//                            ExecutorService executor = Executors.newCachedThreadPool();
//                            executor.submit(r);
                                Thread thread = new Thread(r);
                                thread.setName(threadIp);
                                thread.start();
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        }
                        if (kind == ENTRY_MODIFY) {
                            Log.info(getClass().getName(), "Entry was modified on log dir.:" + event.context() + " :: " + ((Path) event.context()).getFileName());
                            folderUpdate.fileUpdateBusiness(fileName, filePath);
                        }
                        if (kind == ENTRY_DELETE) {
                            // delete from database
                            Log.info(getClass().getName(), "Entry was deleted from log dir.:" + event.context() + " :: " + ((Path) event.context()).getFileName());
                            folderDelete.fileDeleteBusiness(fileName, filePath);
                        }
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
