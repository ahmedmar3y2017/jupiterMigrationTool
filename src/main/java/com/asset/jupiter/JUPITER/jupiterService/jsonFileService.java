package com.asset.jupiter.JUPITER.jupiterService;


import com.asset.jupiter.JUPITER.Dao.jsonFileDao;
import com.asset.jupiter.JUPITER.Model.Entities.JsonFile;
import com.asset.jupiter.Util.exceptions.JupiterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)
public class jsonFileService {

    @Autowired
    jsonFileDao jsonFileDao;

    // save and flush
    public JsonFile saveAndFlush(JsonFile jsonFile) {
        JsonFile file = jsonFileDao.saveAndFlush(jsonFile);

        return file;
    }
    public JsonFile findByInfoFileId(long infoFilId) {

        return jsonFileDao.findById_InfoFileId(infoFilId);

    }


}
