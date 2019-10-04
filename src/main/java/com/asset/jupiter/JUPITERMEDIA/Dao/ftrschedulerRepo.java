package com.asset.jupiter.JUPITERMEDIA.Dao;

import com.asset.jupiter.JUPITERMEDIA.Model.Entities.Ftrscheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ftrschedulerRepo extends JpaRepository<Ftrscheduler, String> {
    List<Ftrscheduler> findByOperationid(String oid);

    @Override
    <S extends Ftrscheduler> S saveAndFlush(S entity);

    // rollback

    @Override
    void deleteById(String s);
}
