package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.ItemIndexclass;
import com.asset.jupiter.JUPITER.Model.Entities.ItemIndexclassPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface itemIndexclassRepo extends JpaRepository<ItemIndexclass, ItemIndexclassPK> {


    @Override
    <S extends ItemIndexclass> S saveAndFlush(S entity);
    // rollback

    @Override
    void deleteById(ItemIndexclassPK itemIndexclassPK);
}
