package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.Knowledgepool;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface knowledgepoolRepo extends CrudRepository<Knowledgepool, String> {
    Knowledgepool findByName(String name);
}
