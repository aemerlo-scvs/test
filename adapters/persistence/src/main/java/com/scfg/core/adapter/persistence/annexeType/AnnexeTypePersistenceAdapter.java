package com.scfg.core.adapter.persistence.annexeType;

import com.scfg.core.adapter.persistence.requirementsTable.RequirementsTableRepository;
import com.scfg.core.application.port.out.AnnexeTypePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.domain.common.AnnexeType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
@PersistenceAdapter
@RequiredArgsConstructor
public class AnnexeTypePersistenceAdapter implements AnnexeTypePort {


    private final AnnexeTypeRepository annexeTypeRepository;
    private final RequirementsTableRepository requirementsTableRepository;


    @Override
    public List<AnnexeType> findAll() {
        Object o = annexeTypeRepository.findAll();
        return (List<AnnexeType>) o;
    }

    @Override
    public AnnexeType findByIdOrExcepcion(Long annexeTypeId) {
       AnnexeTypeJpaEntity annexeTypeJpaEntity = this.annexeTypeRepository.findOptionalById(
                annexeTypeId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue()
        ).orElseThrow(()-> new NotDataFoundException("annexeTypeId: " + annexeTypeId + " Not found"));
       return new ModelMapper().map(annexeTypeJpaEntity, AnnexeType.class);
    }

    @Override
    public AnnexeType findByProductIdAndInternalCode(Long productId, Long internalCode) {
        AnnexeTypeJpaEntity annexeTypeJpaEntity = this.annexeTypeRepository.findByProductIdAndInternalCodeAndStatus(productId,
                internalCode, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        return new ModelMapperConfig().getStrictModelMapper().map(annexeTypeJpaEntity, AnnexeType.class);
    }
}
