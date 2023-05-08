package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ClauseUseCase;
import com.scfg.core.application.port.out.ClausePort;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Clause;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@UseCase
@Slf4j
@RequiredArgsConstructor
public class ClauseService implements ClauseUseCase {
    private final ClausePort clausePort;

    @Override
    public Clause getById(Long id) {
        return clausePort.findById(id);
    }

    @Override
    public List<Clause> getAllClause() {
        return clausePort.findAll();
    }

    @Override
    public List<Clause> getAllClauseByProductId(Long id) {
        return clausePort.findAllClauseByProductId(id);
    }

    @Override
    public PersistenceResponse saveOrUpdate(Clause clause) {
        return clausePort.saveOrUpdate(clause);
    }

    @Override
    public PersistenceResponse delete(Long id) {
        return clausePort.delete(id);
    }
}
