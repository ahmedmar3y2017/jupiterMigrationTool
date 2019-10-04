package com.asset.jupiter.Util.FolderWatcher;

import com.asset.jupiter.JUPITER.Model.Entities.InfoFile;
import com.asset.jupiter.JUPITER.jupiterService.infoFileService;
import com.asset.jupiter.Util.MailConfig.SendMail;
import com.asset.jupiter.Util.MailConfig.mail;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.Date;

@Component
public class folderDelete {


    @Autowired
    infoFileService infoFileService;

    @Autowired
    SendMail sendMail;

    public void fileDeleteBusiness(String fileName, String path) {

        if (fileName.contains(".json") && !fileName.contains("-FAILED")) {
            // get from database by path
            InfoFile byPath = infoFileService.findAllByPath(path);
            if (byPath != null) {
                // if status ready
                if (byPath.getStatus().equalsIgnoreCase(Defines.ready)) {

                    infoFileService.deleteByPath(path);

                }
                // if status INPRocess
                if (byPath.getStatus().equalsIgnoreCase(Defines.inProgress)) {

                    // add to database
                    InfoFile infoFile = new InfoFile("", "",
                            path, new Timestamp(new Date().getTime()),
                            null,
                            null,
                            Defines.ready);

                    infoFileService.saveAndFlush(infoFile);
                    // throw Exception , Send Mail
                    Log.error(folderDelete.class.getName(), "Error Dele File In progress : " + path);
                    Log.debug(folderDelete.class.getName(), "Error Dele File In progress : " + path);
                    // throw Exception

                    // send Mail
//                    try {
//                        sendMail.prepareAndSendTemplate(new mail("", "", "Shared Folder Delete File In process Status \n path : " + path) ,
//                                Defines.errorImage);
//                    } catch (MessagingException e) {
//                        e.printStackTrace();
//                    }


                }

            }
        }

    }
}
