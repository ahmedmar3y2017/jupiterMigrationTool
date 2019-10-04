package com.asset.jupiter.Util.FolderWatcher;

import com.asset.jupiter.JUPITER.Model.Entities.InfoFile;
import com.asset.jupiter.JUPITER.jupiterService.infoFileService;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class folderUpdate {

    @Autowired
    infoFileService infoFileService;

    // 1 - check if .json and !contain -> -Failed
    // 2  - get from database
    // 3 - if found
    // 4 - if status = in progress - >  exception , mail
    // 5 - if ready DO Nothing
    // 6 - if finished Do Nothing
    // 7 - if not found
    // 8 - insert into database
    public void fileUpdateBusiness(String fileName, String path) {

        // check If Json File
        if (fileName.contains(".json") && !fileName.contains("-FAILED")) {

            // get from db
            InfoFile allByPath = infoFileService.findAllByPath(path);
            // if found
            if (allByPath != null) {

                // if inprocessing send mail , throw exception
                if (allByPath.getStatus().equalsIgnoreCase(Defines.inProgress)) {

                    // send mail . throw exceptiomn

                }
                // if ready or finished
                // do nothing
            }
            // not found
            else {
                // insert into database
                InfoFile infoFile = new InfoFile("", "",
                        path, new Timestamp(new Date().getTime()),
                        null,
                        null,
                        Defines.ready);
                CompletableFuture<InfoFile> infoFileCompletableFuture = infoFileService.saveAndFlush(infoFile);
                InfoFile infoFile1 = null;
                try {
                    infoFile1 = infoFileCompletableFuture.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (infoFile1 != null) {
                    // Done inserted
                    Log.info(folderUpdate.class.getName() ,"Done Insert File json into database " + path);
                    Log.debug(folderUpdate.class.getName() ,"Done Insert File json into database " + path);

                }

            }

        }

    }


}
