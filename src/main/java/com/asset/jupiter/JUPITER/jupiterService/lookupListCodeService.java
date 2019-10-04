package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.lookupListCodeRepo;
import com.asset.jupiter.JUPITER.Model.Entities.LookupListCode;
import com.asset.jupiter.Util.exceptions.JupiterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)

public class lookupListCodeService {

    @Autowired
    lookupListCodeRepo lookupListCodeRepo;

    // find by Value
    @Async("jpaAsyncExecutor")
    public CompletableFuture<LookupListCode> findByValue(String listId, String value) {

        LookupListCode byValue = lookupListCodeRepo.findByListIdAndValue(listId, value);

        return CompletableFuture.completedFuture(byValue);

    }
}
