package com.scfg.core.adapter.persistence.annexeRequirementControl;

import com.scfg.core.application.port.out.AnnexeRequirementControlPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.domain.common.RequestAnnexe;
import com.scfg.core.domain.dto.vin.AnnexeRequirementDto;
import com.scfg.core.domain.common.AnnexeRequirementControl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class AnnexeRequirementControlPersistenceAdapter implements AnnexeRequirementControlPort {


    private final AnnexeRequirementControlRepository annexeRequirementControlRepository;

    @Override
    public Long saveOrUpdate(AnnexeRequirementControl annexeRequirementControl) {
        AnnexeRequirementControlJpaEntity annexeRequirementControlJpaEntity = mapToJpaEntity(annexeRequirementControl);
        annexeRequirementControlJpaEntity = annexeRequirementControlRepository.save(annexeRequirementControlJpaEntity);
        return annexeRequirementControlJpaEntity.getId();
    }

    @Override
    public AnnexeRequirementControl findByRequestAnnexeIdAndFileDocumentId(Long requestAnnexeId, Long fileDocumentId) {
        AnnexeRequirementControlJpaEntity annexeRequirementControlJpaEntity = this.annexeRequirementControlRepository.findByRequestAnnexeIdAndFileDocumentId(
                requestAnnexeId, fileDocumentId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return new ModelMapperConfig().getStrictModelMapper().map(annexeRequirementControlJpaEntity, AnnexeRequirementControl.class);
    }

    @Override
    public List<AnnexeRequirementDto> findAllByRequestAnnexeIdAndAnnexeTypeId(Long requestAnnexeId) {
        return this.annexeRequirementControlRepository.finAllByRequestAnnexeIdAndTypeId(
                requestAnnexeId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
    }

    @Override
    public AnnexeRequirementControl getAnnexeRequirementControlByIdOrExcepcion(Long annexeRequirementControlId) {
        AnnexeRequirementControlJpaEntity annexeRequirementControlJpaEntity = this.annexeRequirementControlRepository.getOptionalById(
                annexeRequirementControlId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue())
                .orElseThrow(()-> new NotDataFoundException("annexeRequirementControlId: " + annexeRequirementControlId + " Not found"));
        return new ModelMapper().map(annexeRequirementControlJpaEntity, AnnexeRequirementControl.class);
    }

    private AnnexeRequirementControlJpaEntity mapToJpaEntity(AnnexeRequirementControl annexeRequirement) {

        return new ModelMapperConfig()
                .getStrictModelMapper()
                .map(annexeRequirement, AnnexeRequirementControlJpaEntity.class);

    }

}

