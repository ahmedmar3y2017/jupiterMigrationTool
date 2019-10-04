package com.asset.jupiter.JUPITERMEDIA.jupiterMediaService;

import com.asset.jupiter.JUPITERMEDIA.Dao.mediamanagerRepo;
import com.asset.jupiter.JUPITERMEDIA.Model.Entities.Mediamanager;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "anotherTransactionManager", rollbackFor = JupiterException.class)

public class mediamanagerService {
    @Autowired
    mediamanagerRepo mediamanagerRepo;

//    @Async("jpaMedAsyncExecutor")
    public CompletableFuture<Mediamanager> saveAndFlush(Mediamanager mediamanagerRepos) {
        Mediamanager mediamanager = mediamanagerRepo.saveAndFlush(mediamanagerRepos);

        return CompletableFuture.completedFuture(mediamanager);
    }

    @Async("jpaAsyncExecutor")
    public CompletableFuture<String> findLastItem() {
        String last = mediamanagerRepo.findLastItem();
        return CompletableFuture.completedFuture(last);

    }

}
