package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.Viewfield;
import com.asset.jupiter.JUPITER.Model.Entities.ViewfieldPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface viewFieldDao extends JpaRepository<Viewfield, ViewfieldPK> {

    @Override
    Optional<Viewfield> findById(ViewfieldPK viewfieldPK);
}
