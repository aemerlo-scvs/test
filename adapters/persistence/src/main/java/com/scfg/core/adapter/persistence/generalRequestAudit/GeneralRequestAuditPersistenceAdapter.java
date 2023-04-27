package com.scfg.core.adapter.persistence.generalRequestAudit;

import com.scfg.core.application.port.out.GeneralRequestAuditPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.domain.common.GeneralRequestAudit;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class GeneralRequestAuditPersistenceAdapter implements GeneralRequestAuditPort {

    private final GeneralRequestAuditRepository generalRequestAuditRepository;

    @Override
    public long saveOrUpdate(GeneralRequestAudit generalRequestAudit) {
        GeneralRequestAuditJpaEntity generalRequestAuditJpaEntity = new ModelMapperConfig().getStrictModelMapper()
                .map(generalRequestAudit, GeneralRequestAuditJpaEntity.class);
         generalRequestAuditJpaEntity = generalRequestAuditRepository.save(generalRequestAuditJpaEntity);
         return generalRequestAuditJpaEntity.getId();
    }
}
