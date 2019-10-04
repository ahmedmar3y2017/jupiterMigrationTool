package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.infoFileDao;
import com.asset.jupiter.JUPITER.Model.Entities.InfoFile;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.logging.Log;
import com.asset.jupiter.Web.JasperReports.reportMailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)

public class infoFileService {

    @Autowired
    infoFileDao infoFileDao;


    public CompletableFuture<InfoFile> saveAndFlush(InfoFile infoFile) {
        InfoFile infoFile1 = infoFileDao.saveAndFlush(infoFile);

        return CompletableFuture.completedFuture(infoFile1);
    }

    // set to cach
    public List<InfoFile> findAll() {

        return infoFileDao.findAll();

    }

    public List<InfoFile> findAllByStatus(String status ) {

        return infoFileDao.findAllByStatus(status );

    }
    public InfoFile findAllByPathAndStatus(String path  , String status  ) {

        return infoFileDao.findAllByPathAndStatus(path , status  );

    }
    public Page<InfoFile> findAllByStatus(String status , Pageable pageable) {

        return infoFileDao.findAllByStatus(status , pageable);

    }

    public void updateInfoFile(InfoFile infoFile) {

        infoFileDao.save(infoFile);

    }

    public void deleteByPath(String path) {

        infoFileDao.deleteByPath(path);

    }

    public InfoFile findAllByPath(String path) {

        return infoFileDao.findAllByPath(path);

    }

    // find all to Report
    public CompletableFuture<List> findAllStatusReport() {

        return CompletableFuture.completedFuture(infoFileDao.findAllStatusReports());
    }
}
