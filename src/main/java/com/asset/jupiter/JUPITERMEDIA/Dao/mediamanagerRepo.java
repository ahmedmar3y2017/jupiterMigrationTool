package com.asset.jupiter.JUPITERMEDIA.Dao;

import com.asset.jupiter.JUPITERMEDIA.Model.Entities.Mediamanager;
import com.asset.jupiter.JUPITERMEDIA.Model.Entities.MediamanagerPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface mediamanagerRepo extends JpaRepository<Mediamanager, MediamanagerPK> {
    List<Mediamanager> findById_Objectid(String oid);

    @Override
    <S extends Mediamanager> S saveAndFlush(S entity);

    @Query(
            value = "Select  MediaManager_CNT.NextVal From DUAL",
            nativeQuery = true)
    String findLastItem();

    // rollback
    @Override
    void deleteById(MediamanagerPK mediamanagerPK);


    void deleteById_ObjectidAndId_Objecttype(String objectId, String objectType);
}
