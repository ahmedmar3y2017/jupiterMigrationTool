package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.Link;
import com.asset.jupiter.JUPITER.Model.Entities.LinkPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface linksRepo extends JpaRepository<Link, LinkPK> {
    Link findById_Itemid(String itemId);

    //
    List<Link> findById_ParentidAndIndexclass(String Parentid, String Indexclass);

    List<Link> findById_ParentidAndId_Itemid(String Parentid, String itrmId);



    @Override
    <S extends Link> S saveAndFlush(S entity);

    // rollback

    @Override
    void deleteById(LinkPK linkPK);
}
