package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.itemIndexclassRepo;
import com.asset.jupiter.JUPITER.Model.Entities.ItemIndexclass;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)

public class itemIndexclassService {
    @Autowired
    itemIndexclassRepo itemIndexclassRepo;

//    @Async("jpaAsyncExecutor")
    public CompletableFuture<ItemIndexclass> saveAndFlush(ItemIndexclass itemIndexclass) {
        ItemIndexclass itemIndexclass1 = itemIndexclassRepo.saveAndFlush(itemIndexclass);

        return CompletableFuture.completedFuture(itemIndexclass1);
    }

}
