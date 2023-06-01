package com.scfg.core.adapter.persistence.coveragePlan;

import com.scfg.core.application.port.out.CoveragePlanPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.util.ObjectMapperUtils;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CoveragePlan;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class CoveragePlanPersistenceAdapter implements CoveragePlanPort {
    private final CoveragePlanRepository coveragePlanRepository;
    @Override
    public PersistenceResponse saveOrUpdate(CoveragePlan coveragePlan) {
        CoveragePlanJpaEntity coveragePlanJpaEntity = ObjectMapperUtils.map(coveragePlan, CoveragePlanJpaEntity.class);
        coveragePlanJpaEntity = coveragePlanRepository.save(coveragePlanJpaEntity);
        return new PersistenceResponse(
                CoveragePlanPersistenceAdapter.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                ObjectMapperUtils.map(coveragePlanJpaEntity, CoveragePlan.class));
    }


    @Override
    public PersistenceResponse delete(Long id) {
        CoveragePlanJpaEntity coveragePlanJpaEntity = coveragePlanRepository.findById(id).get();
        coveragePlanJpaEntity.setStatus(0);
        coveragePlanJpaEntity.setLastModifiedAt(new Date());
        coveragePlanJpaEntity = coveragePlanRepository.save(coveragePlanJpaEntity);
        return new PersistenceResponse(CoveragePlanPersistenceAdapter.class.getSimpleName(), ActionRequestEnum.DELETE, coveragePlanJpaEntity);
    }

    @Override
    public CoveragePlan getCoveragePlanByPlanIdAndCoverageId(Long planId, Long coverageId) {
        CoveragePlanJpaEntity coveragePlanJpaEntity = coveragePlanRepository.findByPlanIdAndCoverageId(planId, coverageId);
        return ObjectMapperUtils.map(coveragePlanJpaEntity,CoveragePlan.class);
    }

    @Override
    public List<CoveragePlan> getAllCoveragePlan() {
        List<CoveragePlanJpaEntity> coveragePlanJpaEntities = coveragePlanRepository.findAll();
        return coveragePlanJpaEntities.size() > 0 ? ObjectMapperUtils.mapAll(coveragePlanJpaEntities, CoveragePlan.class) : new ArrayList<>();
    }
    @Override
    public List<CoveragePlan> getAllCoveragePlanByPlanId(Long planId) {
        List<CoveragePlanJpaEntity> coveragePlanJpaEntities = coveragePlanRepository.findALLByPlanId(planId);
        return coveragePlanJpaEntities.size() > 0 ? ObjectMapperUtils.mapAll(coveragePlanJpaEntities, CoveragePlan.class) : new ArrayList<>();
    }

}
