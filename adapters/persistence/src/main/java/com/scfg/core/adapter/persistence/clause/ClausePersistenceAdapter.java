package com.scfg.core.adapter.persistence.clause;

import com.scfg.core.application.port.out.ClausePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ObjectMapperUtils;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Clause;
import com.scfg.core.domain.common.ClassifierType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class ClausePersistenceAdapter implements ClausePort {
    private final ClauseRepository clauseRepository;

    @Override
    public PersistenceResponse saveOrUpdate(Clause clause) {
        ClauseJpaEntity clauseJpaEntity = ObjectMapperUtils.map(clause, ClauseJpaEntity.class);
        clauseJpaEntity = clauseRepository.save(clauseJpaEntity);
        return new PersistenceResponse(
                ClausePersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                ObjectMapperUtils.map(clauseJpaEntity, Clause.class));
    }

    @Override
    public PersistenceResponse delete(Long Id) {
        Clause clause = findById(Id);
        clause.setLastModifiedAt(new Date());
        clause.setStatus(0);
        ClauseJpaEntity clauseJpaEntity = ObjectMapperUtils.map(clause, ClauseJpaEntity.class);
        clauseJpaEntity = clauseRepository.save(clauseJpaEntity);
        return new PersistenceResponse(ClausePersistenceAdapter.class.getSimpleName(), ActionRequestEnum.DELETE, ObjectMapperUtils.map(clauseJpaEntity, Clause.class));
    }

    @Override
    public PersistenceResponse deleteByProductId(Long productId) {
        clauseRepository.deleteByProductId(productId);
        return new PersistenceResponse(
                Clause.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                null
        );
    }

    @Override
    public List<Clause> findAll() {
        List<ClauseJpaEntity> clauseJpaEntities = clauseRepository.findAll();
        return clauseJpaEntities.size() > 0 ? ObjectMapperUtils.mapAll(clauseJpaEntities, Clause.class) : new ArrayList<>();
    }

    @Override
    public List<Clause> findAllClauseByProductId(Long productId) {
        List<ClauseJpaEntity> clauseJpaEntities = clauseRepository.findAllByProductId(productId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return clauseJpaEntities.size() > 0 ? ObjectMapperUtils.mapAll(clauseJpaEntities, Clause.class) : new ArrayList<>();
    }

    @Override
    public Clause findById(Long id) {
        Optional<ClauseJpaEntity> clause = clauseRepository.findById(id);
        return clause.isPresent() ? ObjectMapperUtils.map(clause.get(), Clause.class) : new Clause();
    }
}
