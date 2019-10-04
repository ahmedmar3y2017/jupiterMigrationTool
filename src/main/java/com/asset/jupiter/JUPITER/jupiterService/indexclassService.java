package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.indexclassRepo;
import com.asset.jupiter.JUPITER.Model.Entities.Indexclass;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)

public class indexclassService {
    @Autowired
    indexclassRepo indexclassRepo;

    @Async("jpaAsyncExecutor")
    public CompletableFuture<Indexclass> findByClassname(String name) throws InterruptedException {
        Indexclass indexclass = indexclassRepo.findByClassname(name);

        return CompletableFuture.completedFuture(indexclass);
    }

    @Async("jpaAsyncExecutor")
    public CompletableFuture<Indexclass> findByClassid(String classId) throws InterruptedException {
        Indexclass indexclass = indexclassRepo.findByClassid(classId);

        return CompletableFuture.completedFuture(indexclass);
    }
}
