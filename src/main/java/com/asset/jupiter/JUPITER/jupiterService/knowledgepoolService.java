package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.knowledgepoolRepo;
import com.asset.jupiter.JUPITER.Model.Entities.Knowledgepool;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)

public class knowledgepoolService {
    @Autowired
    knowledgepoolRepo knowledgepoolRepo;

    @Async("jpaAsyncExecutor")

    public CompletableFuture<Knowledgepool> findByName(String name) throws InterruptedException {
        Knowledgepool knowledgepool = knowledgepoolRepo.findByName(name);

        return CompletableFuture.completedFuture(knowledgepool);
    }
}
