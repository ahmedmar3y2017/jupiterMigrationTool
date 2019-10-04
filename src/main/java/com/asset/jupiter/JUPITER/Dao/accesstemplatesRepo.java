package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.Accesstemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface accesstemplatesRepo extends JpaRepository<Accesstemplate,Long> {

    List<Accesstemplate> findByTemplateid(long pid);

    @Override
    <S extends Accesstemplate> S saveAndFlush(S entity);

}
