package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.JsonFile;
import com.asset.jupiter.JUPITER.Model.Entities.JsonFilePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface jsonFileDao extends JpaRepository<JsonFile, JsonFilePK> {

    // save into
    @Override
    <S extends JsonFile> S saveAndFlush(S entity);


    JsonFile findById_InfoFileId(long infoFileId);
}
