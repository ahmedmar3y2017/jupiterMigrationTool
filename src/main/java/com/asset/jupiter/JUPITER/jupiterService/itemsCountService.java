package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.itemsCountRepo;
import com.asset.jupiter.JUPITER.Model.Entities.Itemscount;
import com.asset.jupiter.JUPITER.Model.Entities.ItemscountPK;
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


public class itemsCountService {
    @Autowired
    itemsCountRepo itemsCountRepo;


    @Async("jpaAsyncExecutor")
    public CompletableFuture<Itemscount> findById_parentidAndId_parenttypeAndId_classid(String parentId, long parentType, String classId) throws InterruptedException {

        Itemscount Item = itemsCountRepo.findById_parentidAndId_parenttypeAndId_classid(parentId, parentType, classId);

        return CompletableFuture.completedFuture(Item);
    }

//    @Async("jpaAsyncExecutor")
    public CompletableFuture<Itemscount> save(Itemscount itemscount) {

        Itemscount Item = itemsCountRepo.save(itemscount);

        return CompletableFuture.completedFuture(Item);
    }

//    @Async("jpaAsyncExecutor")
    public CompletableFuture<Itemscount> saveAndFlush(Itemscount itemscount) {

        Itemscount Item = itemsCountRepo.saveAndFlush(itemscount);

        return CompletableFuture.completedFuture(Item);
    }
}
