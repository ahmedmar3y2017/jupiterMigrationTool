package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface fieldDao extends CrudRepository<Field, String> {

    Field findAllByTableIdAndFieldid(String tableId, String fieldId);
}
