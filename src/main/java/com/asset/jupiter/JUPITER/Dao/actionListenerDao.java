package com.asset.jupiter.JUPITER.Dao;

import com.asset.jupiter.JUPITER.Model.Entities.ActionListener;
import com.asset.jupiter.JUPITER.Model.Entities.ActionListenerPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface actionListenerDao extends JpaRepository<ActionListener, ActionListenerPK> {

    @Override
    <S extends ActionListener> S saveAndFlush(S entity);

    @Override
    Optional<ActionListener> findById(ActionListenerPK actionListenerPK);
}
