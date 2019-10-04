package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.fieldDao;
import com.asset.jupiter.JUPITER.Model.Entities.Field;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)

public class fieldService {

    @Autowired
    fieldDao fieldDao;

    @Async("jpaAsyncExecutor")
    public CompletableFuture<Field> findAllByTableIdAndFieldid(String tableId, String fieldId) {

        Field field = fieldDao.findAllByTableIdAndFieldid(tableId, fieldId);

        return CompletableFuture.completedFuture(field);
    }


}
