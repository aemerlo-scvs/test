package com.scfg.core.adapter.persistence.coveragePlan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoveragePlanRepository extends JpaRepository<CoveragePlanJpaEntity,Long> {
    List<CoveragePlanJpaEntity> findALLByPlanId(Long planId);
}
