package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Clause;
import com.scfg.core.domain.common.Direction;

import java.util.List;

public interface ClausePort {
    PersistenceResponse saveOrUpdate(Clause clause);
    PersistenceResponse delete(Long Id);
    List<Clause> findAll();
    List<Clause>findAllClauseByProductId(Long productId);
    Clause findById(Long id);
}
