/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.logging;

/**
 *
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.core.io.ClassPathResource;

public class ThreadLogger {

    private Logger log = null;
    private String logging_Path;
    private String threadId;
    private String jsonName;

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public String getLogging_Path() {
        return logging_Path;
    }

    public void setLogging_Path(String logging_Path) {
        this.logging_Path = logging_Path;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getJsonName() {
        return jsonName;
    }

    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
    }

    public ThreadLogger(String logClass, String log_path) {
        this.setLogging_Path(log_path);
        this.log = getLogger(logClass, log_path);
        this.setThreadId(logClass);
        this.setJsonName(log_path);
    }

    /**
     * get new instance from java Logger
     *
     * @param logClass
     * @param loggingPath
     * @return
     */
    public Logger getLogger(String logClass, String loggingPath) {
        Logger logger = null;
        this.setLogging_Path(loggingPath);
        Properties props = new Properties();
        InputStream input = null;
        try {
            //start property File path

            props.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
//            props.load(new FileInputStream(new ClassPathResource("src/main/resources/application.properties").getPath()));
            String pathLogXml = props.getProperty("tool_resources") + "/log/log4j.xml";
            String pathFolderLog = props.getProperty("tool_resources") + "/log";
            //end
            logger = Logger.getLogger(logClass);
//            String home = System.getProperty("user.dir");
            String home = props.getProperty("tool_resources") + "/log";
            File jupiterLogsFolder = new File(home + File.separator + "ThreadsLogs");
            jupiterLogsFolder.mkdir();
            String logPath = jupiterLogsFolder.getAbsolutePath() + File.separator + loggingPath + "_%g.log";
            FileHandler fileHandler = new FileHandler(logPath, 1048576, 10, true);
            fileHandler.setFormatter(new SingleLineFormatter());
            fileHandler.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return logger;
    }

    /**
     * function used to write the log file where it takes the log entry which
     * will be written to the file
     *
     * @param logEntry
     * @param logger
     */
    private void writeLog(String logEntry, Logger logger) {
        try {
            if (logger != null) {
                logger.logp(Level.SEVERE, "", "", " " + logEntry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to log info
     *
     * @param threadName
     * @param message
     */
    public void info(String threadName, String message) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("thread:");
        logEntry.append(threadName);
        logEntry.append(";");
        logEntry.append(" INFO ");
        logEntry.append(message);
        if (this.getLog() == null) {
            this.setLog(getLogger(this.getThreadId(), this.getLogging_Path()));
        }
        writeLog(logEntry.toString(), this.getLog());
    }

    /**
     * function to log debug
     *
     * @param threadName
     * @param message
     */
    public void debug(String threadName, String message) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("thread:");
        logEntry.append(threadName);
        logEntry.append(";");
        logEntry.append(" DEBUG ");
        logEntry.append(message);
        if (this.getLog() == null) {
            this.setLog(getLogger(this.getThreadId(), this.getLogging_Path()));
        }
        writeLog(logEntry.toString(), this.getLog());
    }

    /**
     * function to log warn
     *
     * @param threadName
     * @param message
     */
    public void warn(String threadName, String message) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("thread:");
        logEntry.append(threadName);
        logEntry.append(";");
        logEntry.append(" WARN ");
        logEntry.append(message);
        if (this.getLog() == null) {
            this.setLog(getLogger(this.getThreadId(), this.getLogging_Path()));
        }
        writeLog(logEntry.toString(), this.getLog());
    }

    /**
     * function to log error
     *
     * @param threadName
     * @param message
     * @param exception
     */
    public void error(String threadName, String message, Exception exception) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("thread:");
        logEntry.append(threadName);
        logEntry.append(";");
        logEntry.append(" ERROR ");
        logEntry.append(message);
        logEntry.append(" Exception:");
        logEntry.append("but failed due to:");
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        exception.printStackTrace(printWriter);
        logEntry.append(result.toString());
        if (this.getLog() == null) {
            this.setLog(getLogger(this.getThreadId(), this.getLogging_Path()));
        }
        writeLog(logEntry.toString(), this.getLog());
    }
    /**
     * function to log error
     * @param threadName
     * @param message 
     */
    public void error(String threadName, String message){
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("thread:");
        logEntry.append(threadName);
        logEntry.append(";");
        logEntry.append(" ERROR ");
        logEntry.append(message);                
        Writer result = new StringWriter();        
        logEntry.append(result.toString());
        if (this.getLog() == null) {
            this.setLog(getLogger(this.getThreadId(), this.getLogging_Path()));
        }
        writeLog(logEntry.toString(), this.getLog());
    }

    /**
     * function to log trace
     *
     * @param threadName
     * @param message
     */
    public void trace(String threadName, String message) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("thread:");
        logEntry.append(threadName);
        logEntry.append(";");
        logEntry.append(" TRACE ");
        if (this.getLog() == null) {
            this.setLog(getLogger(this.getThreadId(), this.getLogging_Path()));
        }
        writeLog(logEntry.toString(), this.getLog());
    }

    /**
     * function to log function calling
     *
     * @param threadName
     * @param methodName
     * @param executionTime
     * @param parameters
     */
    public void logFunctionCalling(String threadName, String methodName, long executionTime, Object[] parameters) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("thread:");
        logEntry.append(threadName);
        logEntry.append(";");
        logEntry.append(" called:");
        logEntry.append(methodName);
        logEntry.append(" and executed in ");
        logEntry.append(executionTime);
        logEntry.append(" milliseconds ");
        logEntry.append("successfully");
        logEntry.append(" *parameters:");
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] != null) {
                logEntry.append(parameters[i].toString());
            } else {
                logEntry.append(parameters[i]);
            }
            if (i == parameters.length - 1) {
                logEntry.append(".");
            } else {
                logEntry.append(",");
            }
        }
        if (this.getLog() == null) {
            this.setLog(getLogger(this.getThreadId(), this.getLogging_Path()));
        }
        writeLog(logEntry.toString(), this.getLog());
    }

    /**
     * function to log functions exceptions
     *
     * @param threadName
     * @param methodName
     * @param executionTime
     * @param exception
     */
    public void LogFunctionExecution(String threadName, String methodName, long executionTime, Exception exception) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("thread:");
        logEntry.append(threadName);
        logEntry.append(";");
        logEntry.append(" called:");
        logEntry.append(methodName);
        logEntry.append(" and executed in ");
        logEntry.append(executionTime);
        logEntry.append(" milliseconds ");
        if (exception == null) {
            logEntry.append("successfully");
        } else {
            logEntry.append("but failed due to:");
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            exception.printStackTrace(printWriter);
            logEntry.append(result.toString());
        }
        if (this.getLog() == null) {
            this.setLog(getLogger(this.getThreadId(), this.getLogging_Path()));
        }
        writeLog(logEntry.toString(), this.getLog());
    }
}
