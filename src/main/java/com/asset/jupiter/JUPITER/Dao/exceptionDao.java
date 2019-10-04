package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.Exception;
import com.asset.jupiter.JUPITER.Model.Entities.ExceptionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface exceptionDao extends JpaRepository<Exception, ExceptionPK> {

    @Override
    <S extends Exception> S saveAndFlush(S entity);
}
