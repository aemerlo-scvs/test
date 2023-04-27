package com.scfg.core.adapter.persistence.annexeRequirement;

import com.scfg.core.application.port.out.AnnexeRequirementPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.common.AnnexeRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class AnnexeRequirementPersistenceAdapter implements AnnexeRequirementPort {
    private final AnnexeRequirementRepository annexeRequirementRepository;

    @Override
    public List<AnnexeRequirement> findAllByAnnexeTypeId(Long annexeTypeId) {
        List<AnnexeRequirementJpaEntity> list = annexeRequirementRepository.findAllByAnnexeTypeIdAndStatus(annexeTypeId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        return list.stream().map(o -> new ModelMapper()
                .map(o, AnnexeRequirement.class)).collect(Collectors.toList());
    }

    @Override
    public List<AnnexeRequirement> findAll() {
        List<AnnexeRequirementJpaEntity> list = annexeRequirementRepository.findAll(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        return list.stream().map(o -> new ModelMapper()
                .map(o, AnnexeRequirement.class)).collect(Collectors.toList());
    }

}
