package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.viewFieldDao;
import com.asset.jupiter.JUPITER.Model.Entities.Viewfield;
import com.asset.jupiter.JUPITER.Model.Entities.ViewfieldPK;
import com.asset.jupiter.Util.exceptions.JupiterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)
public class viewFieldService {

    @Autowired
    viewFieldDao viewFieldDao;

    @Async("jpaAsyncExecutor")
    public CompletableFuture<Viewfield> findById(ViewfieldPK viewfieldPK) {

        Optional<Viewfield> byId = viewFieldDao.findById(viewfieldPK);

        return CompletableFuture.completedFuture(byId.get());

    }

}
