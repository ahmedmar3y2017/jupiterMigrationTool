package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.accessRuleDao;
import com.asset.jupiter.JUPITER.Model.Entities.Accessrule;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)
public class accessRuleService {

    @Autowired
    accessRuleDao accessRuleDao;

//    @Async("jpaAsyncExecutor")
    public CompletableFuture<Accessrule> saveAndFlush(Accessrule accessrule) {

        Accessrule accessrule1 = accessRuleDao.saveAndFlush(accessrule);

        return CompletableFuture.completedFuture(accessrule1);
    }

}
