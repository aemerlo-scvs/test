package com.scfg.core.adapter.persistence.policyItem;

import com.scfg.core.application.port.out.mortgageReliefValidations.PolicyItemPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.PolicyItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class PolicyItemAdapter implements PolicyItemPort {
    private final PolicyItemRepository policyItemRepository;

    @Override
    public PolicyItem saveOrUpdate(PolicyItem policyItem) {
        PolicyItemJpaEntity policyItemJpaEntity = mapToEntity(policyItem);
        policyItemJpaEntity = policyItemRepository.save(policyItemJpaEntity);
        return policyItemJpaEntity.getId() != null ? mapToDomain(policyItemJpaEntity) : null;
    }

    @Override
    public PolicyItem getPolicyItemByGeneralRequestId(Long generalRequestId) {
        PolicyItemJpaEntity policyItemJpaEntity = policyItemRepository.findByGeneralRequestId(generalRequestId);
        return policyItemJpaEntity.getId() != null ? mapToDomain(policyItemJpaEntity) : null;
    }

    @Override
    public PolicyItem findById(Long policyItemId) {
        Optional<PolicyItemJpaEntity> policyItemJpaEntity = policyItemRepository.findById(policyItemId);
        return mapToDomain(policyItemJpaEntity.get());
    }

    @Override
    public PolicyItem findByPolicyIdAndPersonId(Long policyId, Long personId) {
        PolicyItemJpaEntity policyItemJpaEntity = policyItemRepository.findByPolicyIdAndPersonId(policyId, personId);
        return mapToDomain(policyItemJpaEntity);
    }

    //#region Mappers

    private PolicyItem mapToDomain(PolicyItemJpaEntity policyItemJpaEntity) {
        return new ModelMapper().map(policyItemJpaEntity, PolicyItem.class);
    }

    private PolicyItemJpaEntity mapToEntity(PolicyItem policyItem) {
        return new ModelMapper().map(policyItem, PolicyItemJpaEntity.class);
    }
    //#endregion

}
