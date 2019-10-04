package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.Accessrule;
import com.asset.jupiter.JUPITER.Model.Entities.AccessrulePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface accessRuleDao extends JpaRepository<Accessrule, AccessrulePK> {

    @Override
    <S extends Accessrule> S saveAndFlush(S entity);

    // rollback


    @Override
    void deleteById(AccessrulePK accessrulePK);


}
