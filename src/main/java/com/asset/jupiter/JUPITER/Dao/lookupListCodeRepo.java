package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.LookupListCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface lookupListCodeRepo extends JpaRepository<LookupListCode, String> {


    LookupListCode findByListIdAndValue(String listId, String value);
}
