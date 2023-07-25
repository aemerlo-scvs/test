package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Clause;

import java.util.List;

public interface ClauseUseCase {
    Clause getById(Long id);
    List<Clause> getAllClause();
    List<Clause> getAllClauseByProductId(Long id);
    PersistenceResponse saveOrUpdate(Clause clause);
    PersistenceResponse delete(Long id);
}
