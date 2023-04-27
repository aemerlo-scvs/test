package com.scfg.core.adapter.persistence.annexeRequirementControl;

import com.scfg.core.application.port.out.AnnexeRequirementControlPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.dto.vin.RequirementDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@PersistenceAdapter
@RequiredArgsConstructor
public class AnnexeRequirementControlPersistenceAdapter implements AnnexeRequirementControlPort {


    private final AnnexeRequirementControlRepository annexeRequirementControlRepository;

    @Override
    public Long saveOrUpdate(RequirementDTO o, Long fileDocumentId, Long requestAnnexeId) {
        AnnexeRequirementControlJpaEntity annexeRequirementControlJpaEntity = mapToJpaEntity(o, fileDocumentId, requestAnnexeId);
        annexeRequirementControlJpaEntity = annexeRequirementControlRepository.save(annexeRequirementControlJpaEntity);
        return annexeRequirementControlJpaEntity.getId();
    }

    private AnnexeRequirementControlJpaEntity mapToJpaEntity(RequirementDTO annexeRequirement, Long fileDocumentId, Long requestAnnexeId) {
        AnnexeRequirementControlJpaEntity annexeRequirementControlJpaEntity = new ModelMapper()
                .map(annexeRequirement, AnnexeRequirementControlJpaEntity.class);
        annexeRequirementControlJpaEntity.setFileDocumentId(fileDocumentId);
        annexeRequirementControlJpaEntity.setRequestAnnexeId(requestAnnexeId);
        return annexeRequirementControlJpaEntity;
    }
}

