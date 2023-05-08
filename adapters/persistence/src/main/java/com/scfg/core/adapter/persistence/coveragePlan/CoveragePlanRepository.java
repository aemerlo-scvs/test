package com.scfg.core.adapter.persistence.coveragePlan;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoveragePlanRepository extends JpaRepository<CoveragePlanJpaEntity,Long> {
    CoveragePlanJpaEntity findByPlanIdAndCoverageId(Long planId,Long coverageId);
}
