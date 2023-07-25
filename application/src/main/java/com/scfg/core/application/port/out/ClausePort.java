package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Clause;

import java.util.List;

public interface ClausePort {
    PersistenceResponse saveOrUpdate(Clause clause);
    PersistenceResponse delete(Long id);
    PersistenceResponse deleteByProductId(Long productId);
    List<Clause> findAll();
    List<Clause>findAllClauseByProductId(Long productId);
    Clause findById(Long id);

}
