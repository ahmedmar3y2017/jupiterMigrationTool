package com.asset.jupiter.JUPITERMEDIA.jupiterMediaService;

import com.asset.jupiter.JUPITERMEDIA.Model.Entities.MediamanagerPK;
import com.asset.jupiter.Util.configurationService.config;
import com.asset.jupiter.Util.exceptions.JupiterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "anotherTransactionManager", rollbackFor = JupiterException.class)
public class rollBackJupiter4Med {


    @Autowired
    JdbcTemplate jdbcTemplate;
    // properties
    @Autowired
    config config;


    public void rollBack(String itemType, MediamanagerPK mediamanagerPK, String operationId) {

        if (!operationId.equalsIgnoreCase("") && operationId != null) {
//            ftrschedulerRepo.deleteById(operationId); // 2

//            // jdbc
            Object[] params1 = {operationId};

            jdbcTemplate.update("delete from " + config.getJUPITER_MED_SCHEMA() + ".FTRSCHEDULER  where OPERATIONID=?", params1);
        }
        if (mediamanagerPK != null && itemType.equalsIgnoreCase("3")) {
//            mediamanagerRepo.deleteById_ObjectidAndId_Objecttype(mediamanagerPK.getObjectid(), itemType); // 1 if doc only
            Object[] params1 = {mediamanagerPK.getObjectid(), itemType};

            jdbcTemplate.update("delete from " + config.getJUPITER_MED_SCHEMA() + ".MEDIAMANAGER  where OBJECTID=? and OBJECTTYPE=?", params1);

        }
    }

}
