package com.asset.jupiter;

import com.asset.jupiter.Util.ApplicationEvents.*;
import com.asset.jupiter.Util.MailConfig.mail;
import com.asset.jupiter.Util.configurationService.databaseUnicode;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.boot.context.event.*;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class MyApplicationListener implements ApplicationListener {
    // read From Properties File Config
    Properties prop = new Properties();
    InputStream input = null;

    DatabaseConnection instanceConnection;


    public MyApplicationListener() {

        // for property File Load
        try {

            // property File path
            input = new FileInputStream(new ClassPathResource("src/main/resources/application.properties").getPath());

            // load a properties file
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();

            System.exit(0);
            return;
        }
        // init Log
        Log log = new Log(prop.getProperty("tool_resources") + "/log");
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // on startup App before anything
        if (event instanceof ApplicationStartingEvent) {
            Log.info(MyApplicationListener.class.getName(), "Start App Checking Smtp , Database , Shared Folder");
            Log.debug(MyApplicationListener.class.getName(), "Start App Checking Smtp , Database , Shared Folder");
            // check smtp First
            String smtpHostName = prop.getProperty("spring.mail.host");
            String smtpHostusername = prop.getProperty("spring.mail.username");
            String smtpHostpassword = prop.getProperty("spring.mail.password");
            int smtpHostPort = Integer.parseInt(prop.getProperty("spring.mail.port"));

            // to , client name
            String smtpToMail = prop.getProperty("spring.mail.to");
            String smtpClientName = prop.getProperty("spring.mail.clientName");


            //  1 --------------------------- test Smtp
            try {
                Log.info(MyApplicationListener.class.getName(), "Start test Smtp Connection");
                SMTPConnection.testSmtpConnection(smtpHostName,
                        smtpHostusername,
                        smtpHostpassword,
                        smtpHostPort
                );
            } catch (AuthenticationFailedException e) {
                // user , pass , port

                // log
                Log.info(MyApplicationListener.class.getName(), "Error Smtp Connection UserName , password , Port " + e.getMessage());
                Log.error(MyApplicationListener.class.getName(), "Error Smtp Connection UserName , password , Port " + e.getMessage());
                // send mail Exception


                try {
                    throw new JupiterException("Error Smtp Connection UserName , password , Port " + e.getMessage(), JupiterException.Startup_AuthenticationFailedException_Exception);
                } catch (JupiterException e1) {
                    e1.printStackTrace();
                }

                // stop app
                System.exit(0);
                return;
            } catch (MessagingException e) {
                // smtp name , network

                // log
                Log.info(MyApplicationListener.class.getName(), "Error Smtp Connection SmtpName , Network " + e.getMessage());
                Log.error(MyApplicationListener.class.getName(), "Error Smtp Connection SmtpName , Network " + e.getMessage());
                // send mail
                try {
                    throw new JupiterException("Error Smtp Connection SmtpName , Network " + e.getMessage(), JupiterException.Startup_MessagingException_Exception);
                } catch (JupiterException e1) {
                    e1.printStackTrace();
                }


                // stop app
                System.exit(0);
                return;
            }
            // 2 ---------------------- test Database Connection
            String databaseDriver = prop.getProperty("spring.datasource.driver-class-name");
            String databaseUrl = prop.getProperty("spring.datasource.url");
            String databaseUsername = prop.getProperty("spring.datasource.username");
            String databasePassword = prop.getProperty("spring.datasource.password");
            try {
//                DBConnection.checkConnection(databaseDriver,
//                        databaseUrl,
//                        databaseUsername,
//                        databasePassword
//                );
                instanceConnection = DatabaseConnection.getInstance(databaseDriver,
                        databaseUrl,
                        databaseUsername,
                        databasePassword);
            } catch (SQLException e) {
                // connection

                // log
                Log.info(MyApplicationListener.class.getName(), "Error Database Connection Url , username , pass " + e.getMessage());
                Log.error(MyApplicationListener.class.getName(), "Error Database Connection Url , username , pass " + e.getMessage());

                // send mail
                try {
                    sendMailStartup.prepareAndSendTemplate(new mail("", "","Jupiter Migration Tool Service has stopped  - >  " +  e.getMessage() ,
                                    "Kindly be informed That jupiter Migration Tool has stopped because of a Critical error that happened during tool startup" +
                                            " Error Database Connection Url , username , pass " + e.getMessage() +
                                            "  Please handle the issue and the tool will retry again."),
                            smtpClientName, smtpToMail, smtpHostName, smtpHostPort + "", smtpHostusername, smtpHostpassword, "startupErrorTemplate",
                            Defines.errorImage);
                } catch (MessagingException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {
                    throw new JupiterException("Error Database Connection Url , username , pass  " + e.getMessage(), JupiterException.Startup_SQLException_Exception);
                } catch (JupiterException e1) {
                    e1.printStackTrace();
                }
                // stop app
                System.exit(0);
                return;
            } catch (ClassNotFoundException e) {
                // driver

                // log
                Log.info(MyApplicationListener.class.getName(), "Error Database Driver " + e.getMessage());
                Log.error(MyApplicationListener.class.getName(), "Error Database Driver " + e.getMessage());

                // send mail
                try {
                    sendMailStartup.prepareAndSendTemplate(new mail("", "","Jupiter Migration Tool Service has stopped  - >  " +  e.getMessage(),
                                    "Kindly be informed That jupiter Migration Tool has stopped because of a Critical error that happened during tool startup: " +
                                            " Error Database Driver " + e.getMessage() +
                                            " Please handle the issue and the tool will retry again."),
                            smtpClientName, smtpToMail, smtpHostName, smtpHostPort + "", smtpHostusername, smtpHostpassword, "startupErrorTemplate",
                            Defines.errorImage);
                } catch (MessagingException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    throw new JupiterException("Error Database Driver  " + e.getMessage(), JupiterException.Startup_ClassNotFoundException_Exception);
                } catch (JupiterException e1) {
                    e1.printStackTrace();
                }
                // stop app
                System.exit(0);
                return;
            }


            // 3 ------------------------------ check File System Exists

            String shardFolderPath = prop.getProperty("integrationFolderPath");

            try {
                integrationFolderCheck.testSharedFolder(shardFolderPath);
            } catch (JupiterException e) {

                // log
                if (e.getErrCode() == JupiterException.SharedFolderNotExists_Exception) {
                    Log.info(MyApplicationListener.class.getName(), "Error Shared Folder Not Exists Or Not Directory  " + e.getMessage());
                    Log.error(MyApplicationListener.class.getName(), "Error Shared Folder Not Exists Or Not Directory  " + e.getMessage());

                }
                if (e.getErrCode() == JupiterException.SharedFolderNotAccess_Exception) {
                    Log.info(MyApplicationListener.class.getName(), "Error Shared Folder Not Access  " + e.getMessage());
                    Log.error(MyApplicationListener.class.getName(), "Error Shared Folder Not Access  " + e.getMessage());

                }

                // send mail
                try {
                    sendMailStartup.prepareAndSendTemplate(new mail("", "","Jupiter Migration Tool Service  has stopped  - >  " +  e.getMessage() ,
                                    "Kindly be informed That jupiter Migration Tool has stopped because of a Critical error that happened during tool startup: " +
                                            e.getMessage() +
                                            " Please handle the issue and the tool will retry again."),
                            smtpClientName, smtpToMail, smtpHostName, smtpHostPort + "", smtpHostusername, smtpHostpassword, "startupErrorTemplate",
                            Defines.errorImage);
                } catch (MessagingException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                // stop app
                System.exit(0);
                return;
            }
            //  4 ------------------------------ check Tool resources Folder
            String toolResourcesPath = prop.getProperty("tool_resources");
            try {
                toolResourcesCheck.testToolResourcesFolder(toolResourcesPath);
            } catch (JupiterException e) {
                // log
                if (e.getErrCode() == JupiterException.ToolResourcesNotExists_Exception) {
                    Log.info(MyApplicationListener.class.getName(), "Error Tool Resources Not Exists Or Not Directory  " + e.getMessage());
                    Log.error(MyApplicationListener.class.getName(), "Error Tool Resources Not Exists Or Not Directory  " + e.getMessage());

                }
                if (e.getErrCode() == JupiterException.ToolResourcesNotAccess_Exception) {
                    Log.info(MyApplicationListener.class.getName(), "Error Tool Resources Not Access  " + e.getMessage());
                    Log.error(MyApplicationListener.class.getName(), "Error Tool Resources Not Access  " + e.getMessage());

                }

                // send mail
                try {
                    sendMailStartup.prepareAndSendTemplate(new mail("", "","Jupiter Migration Tool Service  has stopped  - >  " +  e.getMessage() ,
                                    "Kindly be informed That jupiter Migration Tool has stopped because of a Critical error that happened during tool startup: " +
                                            e.getMessage() +
                                            " Please handle the issue and the tool will retry again."),
                            smtpClientName, smtpToMail, smtpHostName, smtpHostPort + "", smtpHostusername, smtpHostpassword, "startupErrorTemplate",
                            Defines.errorImage);
                } catch (MessagingException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                // stop app
                System.exit(0);
                return;
            }

            // close read property File
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // finish Test
        }
        if (event instanceof ApplicationStartedEvent) {

            // get unique Code from database
            String databaseDriver = prop.getProperty("spring.datasource.driver-class-name");
            String databaseUrl = prop.getProperty("spring.datasource.url");
            String databaseUsername = prop.getProperty("spring.datasource.username");
            String databasePassword = prop.getProperty("spring.datasource.password");

            try {
                instanceConnection = DatabaseConnection.getInstance(databaseDriver,
                        databaseUrl,
                        databaseUsername,
                        databasePassword);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Connection connection = instanceConnection.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select value" +
                        " from v$nls_parameters" +
                        " where parameter = 'NLS_CHARACTERSET'");
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String value = resultSet.getString("value");
                    // set unicode to static value to use It
                    databaseUnicode.setUnicode(value);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }


}