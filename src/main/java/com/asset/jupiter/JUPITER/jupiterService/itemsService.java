package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.itemRepo;
import com.asset.jupiter.JUPITER.Model.Entities.Item;
//import com.asset.jupiter.Util.exceptions.AsyncExceptionHandler;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)

public class itemsService {
    @Autowired
    itemRepo itemRepo;

    @Async("jpaAsyncExecutor")
    public CompletableFuture<List<Item>> findByLabelAndIndexclass(String label, String indexClass) throws InterruptedException {
        List<Item> Item = itemRepo.findByLabelAndIndexclassAndStatus(label, indexClass,
                new BigDecimal(0));

        return CompletableFuture.completedFuture(Item);
    }

    //    @Async("jpaAsyncExecutor")
//    @org.springframework.transaction.annotation.Transactional(rollbackFor = {JupiterException.class, RuntimeException.class })
    public CompletableFuture<Item> save(Item items) {
        Item Item = itemRepo.saveAndFlush(items);

        return CompletableFuture.completedFuture(Item);
    }

    @Async("jpaAsyncExecutor")
    public CompletableFuture<String> findLastItem() {
        String last = itemRepo.findLastItem();
        return CompletableFuture.completedFuture(last);

    }


    @Async("jpaAsyncExecutor")
    public CompletableFuture<List<Item>> findByLabel(String label) throws InterruptedException {
        List<Item> Item = itemRepo.findByLabelAndStatus(label,
                new BigDecimal(0));
        return CompletableFuture.completedFuture(Item);
    }
}
