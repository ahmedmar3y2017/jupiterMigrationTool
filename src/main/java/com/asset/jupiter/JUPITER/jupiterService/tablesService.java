package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.tablesRepo;
import com.asset.jupiter.JUPITER.Model.Entities.Table;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)

public class tablesService {
    @Autowired
    tablesRepo tablesRepo;

    @Async("jpaAsyncExecutor")
    public CompletableFuture<Table> findById(String id) throws InterruptedException {
        Table tables = tablesRepo.findById(id).get();
        return CompletableFuture.completedFuture(tables);
    }
}
