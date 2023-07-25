package com.scfg.core.application.port.in;

import com.scfg.core.domain.CoveragePlan;

import java.util.List;

public interface CoveragePlanUseCase {
    List<CoveragePlan> getAllCoveragePlanByPlanId(Long planId);
}
