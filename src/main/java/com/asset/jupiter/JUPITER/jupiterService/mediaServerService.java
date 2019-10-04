package com.asset.jupiter.JUPITER.jupiterService;//package com.asset.jupiter.JUPITERMEDIA.jupiterMediaService;

import com.asset.jupiter.JUPITER.Dao.mediaServerRepo;
import com.asset.jupiter.JUPITER.Model.Entities.Mediaserver;
import com.asset.jupiter.Util.exceptions.JupiterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)

public class mediaServerService {
    @Autowired
    com.asset.jupiter.JUPITER.Dao.mediaServerRepo mediaServerRepo;

    @Async("jpaAsyncExecutor")
    public CompletableFuture<Mediaserver> findByMediaserverid(String mediaServerId) {

        Mediaserver byMediaserverid = mediaServerRepo.findByMediaserverid(mediaServerId);

        return CompletableFuture.completedFuture(byMediaserverid);

    }
}
