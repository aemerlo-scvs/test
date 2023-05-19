package com.scfg.core.adapter.persistence.policyItemEconomicReinsurance;

import com.scfg.core.application.port.out.PolicyItemEconomicReinsurancePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.domain.common.PolicyItemEconomicReinsurance;
import com.scfg.core.domain.dto.vin.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class PolicyItemEconomicReinsuranceAdapter implements PolicyItemEconomicReinsurancePort {

    private final PolicyItemEconomicReinsuranceRepository policyItemEconomicReinsuranceRepository;

    @Override
    public List<PolicyItemEconomicReinsurance> findAllByPolicyItemEconomicId(Long policyItemEconomicId) {
        List<PolicyItemEconomicReinsuranceJpaEntity> list = policyItemEconomicReinsuranceRepository.findAllByPolicyItemEconomicIdAndStatus(
                policyItemEconomicId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        return list.stream().map(o -> new ModelMapperConfig().getStrictModelMapper()
                .map(o, PolicyItemEconomicReinsurance.class)).collect(Collectors.toList());
    }

    @Override
    public PolicyItemEconomicReinsurance saveOrUpdate(PolicyItemEconomicReinsurance policyItemEconomicReinsurance) {
        PolicyItemEconomicReinsuranceJpaEntity policyItemEconomicReinsuranceJpaEntity = mapToEntity(policyItemEconomicReinsurance);
        policyItemEconomicReinsuranceJpaEntity = policyItemEconomicReinsuranceRepository.save(policyItemEconomicReinsuranceJpaEntity);
        return policyItemEconomicReinsuranceJpaEntity.getId() != null ? mapToDomain(policyItemEconomicReinsuranceJpaEntity) : null;
    }

    @Override
    public void saveOrUpdateAll(List<PolicyItemEconomicReinsurance> list) {
        List<PolicyItemEconomicReinsuranceJpaEntity> jpaEntityList = list.stream().map(o ->
                new ModelMapperConfig().getStrictModelMapper().map(o, PolicyItemEconomicReinsuranceJpaEntity.class)
        ).collect(Collectors.toList());

        policyItemEconomicReinsuranceRepository.saveAll(jpaEntityList);
    }

    @Override
    public void setStatusAllByPolicyItemEconomicId(Integer status, Long policyItemEconomicId) {
        policyItemEconomicReinsuranceRepository.setStatusByPolicyEconomicId(status, policyItemEconomicId);
    }

    //#region Mappers

    private PolicyItemEconomicReinsurance mapToDomain(PolicyItemEconomicReinsuranceJpaEntity policyItemEconomicReinsuranceJpaEntity) {
        return new ModelMapperConfig().getStrictModelMapper().map(policyItemEconomicReinsuranceJpaEntity, PolicyItemEconomicReinsurance.class);
    }

    private PolicyItemEconomicReinsuranceJpaEntity mapToEntity(PolicyItemEconomicReinsurance policyItemEconomicReinsurance) {
        return new ModelMapperConfig().getStrictModelMapper().map(policyItemEconomicReinsurance, PolicyItemEconomicReinsuranceJpaEntity.class);
    }

    //#endregion

}
