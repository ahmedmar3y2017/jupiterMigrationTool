package com.asset.jupiter.JUPITERMEDIA.jupiterMediaService;

import com.asset.jupiter.JUPITERMEDIA.Dao.ftrschedulerRepo;
import com.asset.jupiter.JUPITERMEDIA.Model.Entities.Ftrscheduler;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "anotherTransactionManager", rollbackFor = JupiterException.class)

public class ftrschedulerService {
    @Autowired
    ftrschedulerRepo ftrschedulerRepo;

//    @Async("jpaMedAsyncExecutor")
    public CompletableFuture<Ftrscheduler> saveAndFlush(Ftrscheduler ftrschedulers) {
        Ftrscheduler ftrscheduler = ftrschedulerRepo.saveAndFlush(ftrschedulers);

        return CompletableFuture.completedFuture(ftrscheduler);
    }
}
