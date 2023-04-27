package com.scfg.core.adapter.persistence.coverageProductPlan;

import com.scfg.core.application.port.out.mortgageReliefValidations.CoverageProductPlanPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.CoverageProductPlan;
import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.dto.credicasas.CoverageClf;
import com.scfg.core.domain.dto.credicasas.groupthefont.ClfProductPlanCoverageDTO;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@PersistenceAdapter
@RequiredArgsConstructor
public class CoverageProductPlanPersistenceAdapter implements CoverageProductPlanPort {
    private final CoverageProductPlanRepository coverageProductPlanRepository;

    @Override
    public CoverageProductPlan findById(Long id) {
        CoverageProductPlanJpaEntity coverageProductPlanJpaEntity = coverageProductPlanRepository.findById(id).orElseThrow(() -> new NotDataFoundException("CoverageProcutPlan " + id + " no se encontro"));
        return coverageProductPlanJpaEntity != null ? mapToDomain(coverageProductPlanJpaEntity) : null;
    }

    @Override
    public CoverageProductPlan findCoverageProductPlanByPlanIdAndProductId(Long productId, Long planId) {
        CoverageProductPlanJpaEntity coverageProductPlanJpaEntity = coverageProductPlanRepository.findCoverageProductPlanByPlanIdAndProductId(productId, planId);
        return coverageProductPlanJpaEntity != null ? mapToDomain(coverageProductPlanJpaEntity) : null;
    }

    @Override
    public List<ClfProductPlanCoverageDTO> findAllByApsCodeAndPlanIdAndPolicyItemId(String apsCode, long planId, long policyItemId) {
        List<ClfProductPlanCoverageDTO> list = coverageProductPlanRepository.findAllCoverageProductPlanByProductIdAndPlanIdAndPolicyItemId(apsCode, planId, policyItemId);
        list.forEach(o -> {
            o.setPolicyItemId(policyItemId);
        });
        return list;
    }

    @Override
    public List<CoverageClf> getAllCoverageClf(Long generalRequest) {
        List<CoverageClf> coverageClves = coverageProductPlanRepository.findAllByGeneralRequestClf(generalRequest, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return coverageClves;
    }

    @Override
    public List<CoverageDTO> findAllByAgreementCode(Integer agreementCode) {
        return coverageProductPlanRepository.findAllByAgreementCodePlan(agreementCode,PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
    }

    @Override
    public List<CoverageDTO> findAllByPolicyItemId(Long policyItemId) {
        return coverageProductPlanRepository.findAllByPolicyItemId(policyItemId,PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
    }

    private CoverageProductPlan mapToDomain(CoverageProductPlanJpaEntity coverageProductPlanJpaEntity) {
        CoverageProductPlan coverageProductPlan = CoverageProductPlan.builder()
                .id(coverageProductPlanJpaEntity.getPlanId())
                .coverageProductId(coverageProductPlanJpaEntity.getCoverageProductId())
                .planId(coverageProductPlanJpaEntity.getPlanId())
                .insuredCapitalCoverage(coverageProductPlanJpaEntity.getInsuredCapitalCoverage())
                .createdAt(coverageProductPlanJpaEntity.getCreatedAt())
                .lastModifiedAt(coverageProductPlanJpaEntity.getLastModifiedAt())
                .build();
        return coverageProductPlan;
    }

    private CoverageProductPlan mapToDomainClf(CoverageProductPlanJpaEntity coverageProductPlanJpaEntity) {
        CoverageProductPlan coverageProductPlan = CoverageProductPlan.builder()
                .id(coverageProductPlanJpaEntity.getId())
                .coverageProductId(coverageProductPlanJpaEntity.getCoverageProductId())
                .planId(coverageProductPlanJpaEntity.getPlanId())
                .insuredCapitalCoverage(coverageProductPlanJpaEntity.getInsuredCapitalCoverage())
                .ageLimitStay(coverageProductPlanJpaEntity.getAgeLimitStay())
                .minimumEntryAge(coverageProductPlanJpaEntity.getMinimumEntryAge())
                .entryAgeLimit(coverageProductPlanJpaEntity.getEntryAgeLimit())
                .rate(coverageProductPlanJpaEntity.getRate())
                .createdAt(coverageProductPlanJpaEntity.getCreatedAt())
                .lastModifiedAt(coverageProductPlanJpaEntity.getLastModifiedAt())
                .build();
        return coverageProductPlan;
    }

    private List<CoverageProductPlan> mapToListDomainClf(List<CoverageProductPlanJpaEntity> coverageProductPlanJpaEntities) {
        List<CoverageProductPlan> coverageProductPlans = new ArrayList<>();
        coverageProductPlanJpaEntities.forEach(x -> {
            coverageProductPlans.add(mapToDomainClf(x));
        });
        return coverageProductPlans;
    }
}
