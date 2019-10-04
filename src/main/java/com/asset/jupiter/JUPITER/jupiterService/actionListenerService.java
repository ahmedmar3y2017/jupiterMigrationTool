package com.asset.jupiter.JUPITER.jupiterService;

import com.asset.jupiter.JUPITER.Dao.actionListenerDao;
import com.asset.jupiter.JUPITER.Model.Entities.ActionListener;
import com.asset.jupiter.JUPITER.Model.Entities.ActionListenerPK;
import com.asset.jupiter.Util.exceptions.JupiterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(value = "transactionManager", rollbackFor = JupiterException.class)
public class actionListenerService {

    @Autowired
    actionListenerDao actionListenerDao;

    // save and flush
    public ActionListener saveAndFlush(ActionListener actionListener) {
        ActionListener actionListener1 = actionListenerDao.saveAndFlush(actionListener);

        return actionListener1;
    }

    public ActionListener findById(ActionListenerPK actionListenerPK) {

        Optional<ActionListener> byId = actionListenerDao.findById(actionListenerPK);

        return byId.get();
    }
}
