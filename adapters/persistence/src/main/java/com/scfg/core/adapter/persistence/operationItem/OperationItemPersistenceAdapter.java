package com.scfg.core.adapter.persistence.operationItem;

import com.scfg.core.application.port.out.OperationItemPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.common.OperationItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@PersistenceAdapter
@RequiredArgsConstructor
public class OperationItemPersistenceAdapter implements OperationItemPort {

    private final OperationItemRepository operationItemRepository;

    @Override
    public Long saveOrUpdate(OperationItem operationItem) {
        OperationItemJpaEntity operationItemJpaEntity = operationItemRepository.save(new ModelMapper().map(operationItem, OperationItemJpaEntity.class));
        return operationItemJpaEntity.getId();
    }

    @Override
    public OperationItem findByOperationHeaderIdAndMonthAndYear(Long operationHeaderId, Integer month, Integer year) {
        OperationItemJpaEntity operationItemJpaEntity = operationItemRepository.findFirstByOperationHeaderIdAndMonthIdcAndYearIdc(operationHeaderId, month, year);
        return (operationItemJpaEntity != null) ? new ModelMapper().map(operationItemJpaEntity, OperationItem.class) : null;
    }
}
