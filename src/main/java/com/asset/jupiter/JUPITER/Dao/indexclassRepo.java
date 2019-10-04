package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.Indexclass;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface indexclassRepo extends CrudRepository<Indexclass, String> {
    Indexclass findByClassname(String className);

    Indexclass findByClassid(String classID);


}
