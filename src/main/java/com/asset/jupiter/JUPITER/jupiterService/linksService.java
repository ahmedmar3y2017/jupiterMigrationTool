package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.linksRepo;
import com.asset.jupiter.JUPITER.Model.Entities.Link;
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

public class linksService {
    @Autowired
    linksRepo linksRepo;

    @Async("jpaAsyncExecutor")
    public CompletableFuture<List<Link>> findByParentidAndiAndLinks(String Parentid, String indexclass) throws InterruptedException {
        List<Link> Link = linksRepo.findById_ParentidAndIndexclass(Parentid, indexclass);
        return CompletableFuture.completedFuture(Link);
    }

    @Async("jpaAsyncExecutor")
    public CompletableFuture<List<Link>> findById_ParentidAndId_Itemid(String Parentid, String itemId) throws InterruptedException {
        List<Link> Link = linksRepo.findById_ParentidAndId_Itemid(Parentid, itemId);
        return CompletableFuture.completedFuture(Link);
    }

    @Async("jpaAsyncExecutor")
    public CompletableFuture<Link> findById_Itemid(String itemId) throws InterruptedException {
        Link Link = linksRepo.findById_Itemid( itemId);
        return CompletableFuture.completedFuture(Link);
    }

    //    @Async("jpaAsyncExecutor")
    public CompletableFuture<Link> saveAndFlush(Link links) {
        Link Link = linksRepo.saveAndFlush(links);

        return CompletableFuture.completedFuture(Link);
    }
}
