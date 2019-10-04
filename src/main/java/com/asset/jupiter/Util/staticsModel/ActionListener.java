package com.asset.jupiter.Util.staticsModel;

import com.asset.jupiter.JUPITER.Model.Entities.InfoFile;
import com.asset.jupiter.Util.MailConfig.mail;
import com.asset.jupiter.Util.MailConfig.SendMail;
import com.asset.jupiter.Util.defines.Defines;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.util.Date;
import java.util.HashMap;


@Scope(value = "prototype", proxyMode = ScopedProxyMode.DEFAULT)
@Component
// model TO Perfoem statics and logs in code tool
public class ActionListener {


    private int successFolders;
    private int successDocuments;
    private int failureFolders;
    private int failureDocuments;

    private HashMap<Integer, Integer> exceptionCount;
    // configuration Check
    @Autowired
    com.asset.jupiter.Util.configurationService.config config;
    @Autowired
    SendMail sendMail;

    @PostConstruct
    public void init() {

        // if exists return it
        if (exceptionCount == null) {
            exceptionCount = new HashMap<>();

        }
    }

    public ActionListener() {

    }

    // add method to exception Count
    public void add(int exceptionCode , InfoFile infoFile) {

        // check if exceptionCount is null mcreate new Instance 

        // continue
        // get exception Count
        Integer count = exceptionCount.get(exceptionCode);
        // insert new exception Code into hashMap
        if (count == null) {
            // add new in hashMap
            exceptionCount.put(exceptionCode, 1);

        } else {
            // update exception
            exceptionCount.replace(exceptionCode, ++count);
            // check If Count Of Exception > 5
            if (count > Integer.parseInt(config.getNumOfException())) {
                // notify mail
                try {
                    String infoFileName= infoFile.getPath().substring(infoFile.getPath().lastIndexOf("\\") + 1, infoFile.getPath().lastIndexOf("."));

                    sendMail.prepareAndSendTemplate(new mail("", "",infoFileName+" - > Exception Count has been exceeded " ,
                                    "There Are Exception Occurs during processing json File : " +
                                            "" + "Kindly Check the Jupiter Tool Log File at " + new Date()
                                            + "    Exception Details : " + exceptionCode + "   >   "
                                            + config.getNumOfException()) ,
                            Defines.errorImage ,
                            true , infoFile.getPath());
                    //infoFile.getPath()
                } catch (MessagingException e) {
                    e.printStackTrace();
                }

            }

        }

    }


    public int getSuccessFolders() {
        return successFolders;
    }

    public void setSuccessFolders(int successFolders) {
        this.successFolders = successFolders;
    }

    public int getSuccessDocuments() {
        return successDocuments;
    }

    public void setSuccessDocuments(int successDocuments) {
        this.successDocuments = successDocuments;
    }

    public int getFailureFolders() {
        return failureFolders;
    }

    public void setFailureFolders(int failureFolders) {
        this.failureFolders = failureFolders;
    }

    public int getFailureDocuments() {
        return failureDocuments;
    }

    public void setFailureDocuments(int failureDocuments) {
        this.failureDocuments = failureDocuments;
    }

    public HashMap<Integer, Integer> getExceptionCount() {
        return exceptionCount;
    }

    public void setExceptionCount(HashMap<Integer, Integer> exceptionCount) {
        this.exceptionCount = exceptionCount;
    }

// to string for this

    @Override
    public String toString() {
        return "ActionListener{" +
                "successFolders=" + successFolders +
                ", successDocuments=" + successDocuments +
                ", failureFolders=" + failureFolders +
                ", failureDocuments=" + failureDocuments +
                ", exceptionCount=" + exceptionCount +
                '}';
    }

    public void resetValues() {


    }
}
