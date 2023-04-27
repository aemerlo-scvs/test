package com.scfg.core.adapter.persistence.coveragePolicyItem;

import com.scfg.core.application.port.out.mortgageReliefValidations.CoveragePolicyItemPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.CoveragePolicyItem;
import com.scfg.core.domain.GeneralRequest;
import com.scfg.core.domain.Plan;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class CoveragePolicyItemAdapter implements CoveragePolicyItemPort {
    private final CoveragePolicyItemRepository coveragePolicyItemRepository;

    @Override
    public CoveragePolicyItem save(CoveragePolicyItem coveragePolicyItem) {
        CoveragePolicyItemJpaEntity coveragePolicyItemJpaEntity = mapToEntity(coveragePolicyItem);
        coveragePolicyItemJpaEntity = coveragePolicyItemRepository.save(coveragePolicyItemJpaEntity);

        return coveragePolicyItemJpaEntity.getId() != null ? mapToDomain(coveragePolicyItemJpaEntity) : null;
    }

    @Override
    public List<CoveragePolicyItem> saveOrUpdateAll(List<CoveragePolicyItem> coveragePolicyItemList) {
        List<CoveragePolicyItemJpaEntity> coveragePolicyItemJpaEntities = mapListToEntity(coveragePolicyItemList);
        coveragePolicyItemJpaEntities = coveragePolicyItemRepository.saveAll(coveragePolicyItemJpaEntities);
        List<CoveragePolicyItem> coveragePolicyItemListNew = mapListToDomain(coveragePolicyItemJpaEntities);
        return coveragePolicyItemListNew;
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

    private CoveragePolicyItemJpaEntity mapToEntity(CoveragePolicyItem coveragePolicyItem) {
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

    private List<CoveragePolicyItemJpaEntity> mapListToEntity(List<CoveragePolicyItem> policyItems) {
        List<CoveragePolicyItemJpaEntity> coveragePolicyItemJpaEntities = new ArrayList<>();
        policyItems.forEach(x -> {
            coveragePolicyItemJpaEntities.add(mapToEntity(x));
        });
        return coveragePolicyItemJpaEntities;
    }

    private List<CoveragePolicyItem> mapListToDomain(List<CoveragePolicyItemJpaEntity> policyItems) {
        List<CoveragePolicyItem> coveragePolicyItemList = new ArrayList<>();
        policyItems.forEach(x -> {
            coveragePolicyItemList.add(mapToDomain(x));
        });
        return coveragePolicyItemList;
    }
}
