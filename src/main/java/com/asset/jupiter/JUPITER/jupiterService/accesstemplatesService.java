package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.accesstemplatesRepo;
import com.asset.jupiter.JUPITER.Model.Entities.Accesstemplate;
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

public class accesstemplatesService {
    @Autowired
    accesstemplatesRepo accesstemplatesRepo;

    @Async("jpaAsyncExecutor")
    public CompletableFuture<List<Accesstemplate>> findByTemplateid(long id)  {
        List<Accesstemplate> accesstemplates = accesstemplatesRepo.findByTemplateid(id);
        return CompletableFuture.completedFuture(accesstemplates);
    }

//    @Async("jpaAsyncExecutor")
    public CompletableFuture<Accesstemplate> saveAndFlush(Accesstemplate Accesstemplate) {
        Accesstemplate accesstemplates = accesstemplatesRepo.saveAndFlush(Accesstemplate);
        return CompletableFuture.completedFuture(accesstemplates);
    }
}
