package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.InfoFile;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Web.JasperReports.reportMailModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public interface infoFileDao extends JpaRepository<InfoFile, Long> {

    @Override
    <S extends InfoFile> S saveAndFlush(S entity);

    @Override
    List<InfoFile> findAll();

    @Override
    <S extends InfoFile> S save(S entity);

    // ajax by Status
    Page<InfoFile> findAllByStatus(String status , Pageable pageable);
    // ajax search by path , Status
    InfoFile findAllByPathAndStatus(String path , String status );

    List<InfoFile> findAllByStatus(String status);

    InfoFile findAllByPath(String path);


    void deleteByPath(String path);

    @Query(value = "select * from MG_INFO_FILES u where u.STATUS in ('" + Defines.finished + "', '" + Defines.inProgress + "')",
            nativeQuery = true)
    List<InfoFile> findAllStatusReport();

    @Query(value = "select u.PATH , u.STATUS , u.ADDING_DATE , j.TYPE ,j.COUNT_FOLDERS ,j.COUNT_DOCUMENTS from MG_INFO_FILES u , MG_JSON_FILES j where u.INFO_ID=j.INFO_FILE_ID AND u.STATUS in ('" + Defines.finished + "', '" + Defines.inProgress + "')",
            nativeQuery = true)
    List findAllStatusReports();
}
