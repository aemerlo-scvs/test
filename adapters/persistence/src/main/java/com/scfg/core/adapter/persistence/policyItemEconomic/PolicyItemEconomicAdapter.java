package com.scfg.core.adapter.persistence.policyItemEconomic;

import com.scfg.core.application.port.out.PolicyItemEconomicPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.domain.common.PolicyItemEconomic;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class PolicyItemEconomicAdapter implements PolicyItemEconomicPort {

    private final PolicyItemEconomicRepository policyItemEconomicRepository;

    @Override
    public PolicyItemEconomic findLastByPolicyItemIdAndMovementTypeIdc(Long policyItemId, Integer movementTypeIdc) {
        List<PolicyItemEconomicJpaEntity> list = policyItemEconomicRepository.findAllByPolicyItemIdAndMovementTypeIdc(policyItemId,
                movementTypeIdc);

        if (list.isEmpty()) {
            return null;
        }

        return mapToDomain(list.get(0));
    }

    @Override
    public PolicyItemEconomic findLastByPolicyItemIdAndMovementTypeIdc(Long policyItemId, Integer movementTypeIdc, Integer status) {
        List<PolicyItemEconomicJpaEntity> list = policyItemEconomicRepository.findAllByPolicyItemIdAndMovementTypeIdc(policyItemId,
                movementTypeIdc, status);

        if (list.isEmpty()) {
            return null;
        }

        return mapToDomain(list.get(0));
    }

    @Override
    public PolicyItemEconomic saveOrUpdate(PolicyItemEconomic policyItemEconomic) {
        PolicyItemEconomicJpaEntity policyItemEconomicJpaEntity = mapToEntity(policyItemEconomic);
        policyItemEconomicJpaEntity = policyItemEconomicRepository.save(policyItemEconomicJpaEntity);
        return policyItemEconomicJpaEntity.getId() != null ? mapToDomain(policyItemEconomicJpaEntity) : null;
    }

    //#region Mappers

    private PolicyItemEconomic mapToDomain(PolicyItemEconomicJpaEntity policyItemEconomicJpaEntity) {
        return new ModelMapperConfig().getStrictModelMapper().map(policyItemEconomicJpaEntity, PolicyItemEconomic.class);
    }

    private PolicyItemEconomicJpaEntity mapToEntity(PolicyItemEconomic policyItemEconomic) {
        return new ModelMapperConfig().getStrictModelMapper().map(policyItemEconomic, PolicyItemEconomicJpaEntity.class);
    }

    //#endregion

}
