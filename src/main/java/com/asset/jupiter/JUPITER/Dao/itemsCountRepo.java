package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.Itemscount;
import com.asset.jupiter.JUPITER.Model.Entities.ItemscountPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface itemsCountRepo extends JpaRepository<Itemscount, ItemscountPK> {

    Itemscount findById_parentidAndId_parenttypeAndId_classid(String parentId, long parentType, String classId);

    @Override
    <S extends Itemscount> S save(S entity);

    @Override
    <S extends Itemscount> S saveAndFlush(S entity);

    // rollback
    // 1 - find
    // 2 - save -- >  update
    @Override
    Optional<Itemscount> findById(ItemscountPK itemscountPK);
}
