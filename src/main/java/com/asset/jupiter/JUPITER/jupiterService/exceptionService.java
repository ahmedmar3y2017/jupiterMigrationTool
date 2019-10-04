package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.exceptionDao;
import com.asset.jupiter.JUPITER.Model.Entities.Exception;
import com.asset.jupiter.Util.exceptions.JupiterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)

public class exceptionService {

    @Autowired
    exceptionDao exceptionDao;

    // save and flush
    public Exception saveAndFlush(Exception e) {
        Exception exception = exceptionDao.saveAndFlush(e);

        return exception;
    }


}
