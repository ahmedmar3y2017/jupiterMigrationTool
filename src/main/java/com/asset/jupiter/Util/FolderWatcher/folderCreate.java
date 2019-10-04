package com.asset.jupiter.Util.FolderWatcher;

import com.asset.jupiter.AppThread;
import com.asset.jupiter.JUPITER.Model.Entities.InfoFile;
import com.asset.jupiter.JUPITER.jupiterService.infoFileService;
import com.asset.jupiter.Runner;
import com.asset.jupiter.Util.CrunchifyGetIPHostname;
import com.asset.jupiter.Util.MailConfig.SendMail;
import com.asset.jupiter.Util.MailConfig.mail;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class folderCreate {
    // 1 - must .json , not -failed
    // 2 - insert into database

    @Autowired
    infoFileService infoFileService;
    @Autowired
    CrunchifyGetIPHostname crunchifyGetIPHostname;
    //    @Autowired
//    Runner runner;
    @Autowired
    AppThread appThread;
    @Autowired
    SendMail sendMail;

    public InfoFile fileCreateBusiness(String fileName, String path) throws ExecutionException, InterruptedException {

        // must .json , not -failed
        if (fileName.contains(".json") && !fileName.contains("-FAILED")) {
            InfoFile infoFile = new InfoFile("", "",
                    path, new Timestamp(new Date().getTime()),
                    null,
                    null,
                    Defines.ready);
            CompletableFuture<InfoFile> infoFileCompletableFuture = infoFileService.saveAndFlush(infoFile);
            InfoFile infoFile1 = infoFileCompletableFuture.get();
            if (infoFile1 != null) {
                // Done inserted
                Log.info(folderCreate.class.getName(), "Done Insert File json into database " + path);
                Log.debug(folderCreate.class.getName(), "Done Insert File json into database " + path);
                // select by Path to get id
                InfoFile byPath = infoFileService.findAllByPath(infoFile1.getPath());

                return byPath;
            } else {
                return null;
            }

        } else {
            return null;
        }
    }
}
