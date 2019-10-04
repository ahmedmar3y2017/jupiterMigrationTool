package com.asset.jupiter.Util.logging;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.extras.DOMConfigurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;


@Component
public final class Log {

    // Root logger
    private static Logger logger;
    private static String spaces = "";

    public Log() throws IOException {
        Properties props = new Properties();

        props.load(getClass().getClassLoader().getResourceAsStream("application.properties"));

        String pathLogXml = props.getProperty("tool_resources") + "/log/log4j.xml";
        String pathFolderLog = props.getProperty("tool_resources") + "/log";
        Log.initialize(pathFolderLog, pathLogXml);
    }

    public Log(@Value("#{tool_resources}") String folderPath) {

        String pathLogXml = folderPath + "/log4j.xml";
//        String pathFolderLog = new ClassPathResource("log").getPath();
        Log.initialize(folderPath, pathLogXml);
    }


    public static void initialize(String logFilePath, String configFilePath) {

        // Override log file path
        if (logFilePath != null) {
            System.setProperty("logFilePath", logFilePath);
        }

        // Configure logger
        try {
            DOMConfigurator.configure(configFilePath);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        // Set logger
        logger = LogManager.getLogger("com.asset.jupiter");
    }

    public static void trace(String message) {

        setLoggerClass();
        if (logger == null) {
            System.out.println(message);
        } else {
            logger.trace(message);
        }
    }

//    public static void trace(String message, Throwable e) {
//        setLoggerClass();
//        if (logger == null) {
//            System.out.println(message);
//        } else {
//            logger.trace(message, e);
//        }
//    }

    public static void debug(String threadName, String message) {

        setLoggerClass();
        if (logger == null) {
            System.out.println(threadName + " :: " + message);
        } else {
            logger.debug(threadName + " :: " + message);
        }
    }

    public static void info(String threadName, String message) {

        setLoggerClass();
        if (logger == null) {
            System.out.println(threadName + " :: " + message);
        } else {
            logger.info(threadName + " :: " + message);
        }
    }
    public static void time(String methodName, String time){
        setLoggerClass();
        if (logger == null) {
            System.out.println(methodName + " :: " + time);
        } else {
            logger.info(methodName + " :: " + time);
        }
    }

    public static void warn(String threadName, String message) {

        setLoggerClass();
        if (logger == null) {
            System.err.println(threadName + " :: " + message);
        } else {
            logger.warn(threadName + " :: " + message);
        }
    }

    public static void error(String message, Throwable e) {

        setLoggerClass();
        if (logger == null) {
            System.err.println(spaces + message);
            System.err.println(e.getMessage());
        } else {
            logger.error(spaces + message, e);
        }
    }

    public static void error(String threadName, String message) {

        setLoggerClass();
        if (logger == null) {
            System.err.println(threadName + " :: " + message);
        } else {
            logger.error(threadName + " :: " + message);
        }
    }

    public static void error(String threadName, String message, Throwable e) {

        setLoggerClass();
        if (logger == null) {
            System.err.println(threadName + " :: " + message);
        } else {
            logger.error(threadName + " :: " + message, e);
        }
    }

    public static void fatal(String message) {

        setLoggerClass();
        if (logger == null) {
            System.err.println(message);
        } else {
            logger.fatal(message);
        }
    }

    private static void setLoggerClass() {

        String className = Thread.currentThread().getStackTrace()[3].getClassName();
        logger = LogManager.getLogger(className);
    }
    private static void setTimeLoggerClass(){
        String className = Thread.currentThread().getStackTrace()[3].getClassName();
        logger = LogManager.getLogger(className);
        LogMethodTime logTime = new LogMethodTime();
        logger.addAppender(logTime);
    }
}