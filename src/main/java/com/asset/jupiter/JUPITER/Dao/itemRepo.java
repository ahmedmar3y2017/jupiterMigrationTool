package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
@Repository

public interface itemRepo extends JpaRepository<Item, String> {

    List<Item> findByItemidAndStatus(String itemId , BigDecimal status);

    List<Item> findByTypeAndStatus(Byte type, BigDecimal status);

    List<Item> findByLabelAndIndexclassAndStatus(String label, String indexClass , BigDecimal status);

    List<Item> findByLabelAndStatus(String label, BigDecimal status);


    @Override
    <S extends Item> S saveAndFlush(S entity);

    // get last item inserted
    //Select " + seqName + ".NextVal From DUAL
    //select max(to_number(regexp_substr(ITEMID, '\d+'))) ITEMID from ITEMS
    @Query(
            value = "Select  ITEMS_CNT.NextVal From DUAL",
            nativeQuery = true)
    String findLastItem();

    // rollback


    @Override
    void deleteById(String s);

    @Override
    void delete(Item entity);
}
