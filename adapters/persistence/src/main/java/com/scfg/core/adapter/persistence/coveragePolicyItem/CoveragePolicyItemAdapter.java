package com.scfg.core.adapter.persistence.coveragePolicyItem;

import com.scfg.core.application.port.out.mortgageReliefValidations.CoveragePolicyItemPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.domain.CoveragePolicyItem;
import com.scfg.core.domain.Plan;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class CoveragePolicyItemAdapter implements CoveragePolicyItemPort {
    private final CoveragePolicyItemRepository coveragePolicyItemRepository;

    @Override
    public CoveragePolicyItem save(CoveragePolicyItem coveragePolicyItem) {
        CoveragePolicyItemJpaEntity coveragePolicyItemJpaEntity = mapToJpaEntity(coveragePolicyItem);
        coveragePolicyItemJpaEntity = coveragePolicyItemRepository.save(coveragePolicyItemJpaEntity);

        return coveragePolicyItemJpaEntity.getId() != null ? mapToDomain(coveragePolicyItemJpaEntity) : null;
    }

    @Override
    public List<CoveragePolicyItem> saveOrUpdateAll(List<CoveragePolicyItem> coveragePolicyItemList) {
        List<CoveragePolicyItemJpaEntity> jpaEntityList = mapListToJpaEntity(coveragePolicyItemList);
        jpaEntityList = coveragePolicyItemRepository.saveAll(jpaEntityList);

        return mapListToDomain(jpaEntityList);
    }

    @Override
    public List<CoveragePolicyItem> findByPolicyItemId(Long policyItemId) {
        List<CoveragePolicyItemJpaEntity> coveragePolicyItemJpaEntities = coveragePolicyItemRepository.findAllByPolicyItemId(policyItemId);
        return mapListToDomain(coveragePolicyItemJpaEntities);
    }

    @Override
    public void deleteAllByPolicyItemId(long policyItemId) {
        coveragePolicyItemRepository.customDeleteAllByPolicyItemId(policyItemId);
    }

    @Override
    public List<CoveragePolicyItem> findAllByPersonId(Long personId) {
        return mapListToDomain(coveragePolicyItemRepository.findAllByPersonId(personId));
    }

    @Override
    public List<CoveragePolicyItem> findAllByPersonIdGEL(Long personId, List<Plan> planList) {
        List<CoveragePolicyItem> coveragePolicyItemList = new ArrayList<>();
        for (Plan plan : planList) {
            coveragePolicyItemList.addAll(mapListToDomain(coveragePolicyItemRepository.findAllByPersonIdGEL(personId, plan.getId(),
                    PersistenceStatusEnum.CREATED_OR_UPDATED.getValue())));
        }
        return coveragePolicyItemList;
    }

    @Override
    public Double getInsuredCapitalCededByPolicyItemId(Long policyItemId) {
        Double insuredCapitalCeded = this.coveragePolicyItemRepository.getInsuredCapitalCededByPolicyItem(policyItemId);
        if(insuredCapitalCeded == null) return (double) 0;
        else return  insuredCapitalCeded;
    }
    @Override
    public Double getIreByPolicyItemId(Long policyItemId) {
        Double ire = this.coveragePolicyItemRepository.getIreByPolicyItem(policyItemId);
        if(ire == null) return (double) 0;
        else return ire;
    }

    //#region Mappers

    private CoveragePolicyItem mapToDomain(CoveragePolicyItemJpaEntity coveragePolicyItemJpaEntity) {
        CoveragePolicyItem coveragePolicyItem = CoveragePolicyItem.builder()
                .id(coveragePolicyItemJpaEntity.getId())
                .policyItemId(coveragePolicyItemJpaEntity.getPolicyItemId())
                .coverageProductPlanId(coveragePolicyItemJpaEntity.getCoverageProductPlanId())
                .insuredCapital(coveragePolicyItemJpaEntity.getInsuredCapital())
                .additionalPremiumPerPercentage(coveragePolicyItemJpaEntity.getAdditionalPremiumPerPercentage())
                .additionalPremiumPerThousand(coveragePolicyItemJpaEntity.getAdditionalPremiumPerThousand())
                .comment(coveragePolicyItemJpaEntity.getComment())
                .rate(coveragePolicyItemJpaEntity.getRate())
                .createdAt(coveragePolicyItemJpaEntity.getCreatedAt())
                .lastModifiedAt(coveragePolicyItemJpaEntity.getLastModifiedAt())
                .build();
        return coveragePolicyItem;

    }

    private CoveragePolicyItemJpaEntity mapToJpaEntity(CoveragePolicyItem coveragePolicyItem) {
        CoveragePolicyItemJpaEntity coveragePolicyItemJpaEntity = CoveragePolicyItemJpaEntity.builder()
                .coverageProductPlanId(coveragePolicyItem.getCoverageProductPlanId())
                .policyItemId(coveragePolicyItem.getPolicyItemId())
                .insuredCapital(coveragePolicyItem.getInsuredCapital())
                .additionalPremiumPerPercentage(coveragePolicyItem.getAdditionalPremiumPerPercentage())
                .additionalPremiumPerThousand(coveragePolicyItem.getAdditionalPremiumPerThousand())
                .comment(coveragePolicyItem.getComment())
                .rate(coveragePolicyItem.getRate())
                .build();
        return coveragePolicyItemJpaEntity;
    }

    private List<CoveragePolicyItemJpaEntity> mapListToJpaEntity(List<CoveragePolicyItem> list) {
        return list.stream()
                .map(o -> new ModelMapperConfig().getStrictModelMapper().map(o, CoveragePolicyItemJpaEntity.class))
                .collect(Collectors.toList());
    }

    private List<CoveragePolicyItem> mapListToDomain(List<CoveragePolicyItemJpaEntity> jpaEntityList) {
        return jpaEntityList.stream()
                .map(o -> new ModelMapperConfig().getStrictModelMapper().map(o, CoveragePolicyItem.class))
                .collect(Collectors.toList());
    }

    //#endregion

}
