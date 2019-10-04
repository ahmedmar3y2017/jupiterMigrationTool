package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.Mediaserver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface mediaServerRepo extends CrudRepository<Mediaserver, String> {
    List<Mediaserver> findByUsername(String UserName);

    Mediaserver findByMediaserverid(String mediaServerId);


}
