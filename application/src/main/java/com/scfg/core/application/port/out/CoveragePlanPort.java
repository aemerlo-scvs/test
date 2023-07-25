package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CoveragePlan;

import java.util.List;

public interface CoveragePlanPort {
    PersistenceResponse save(CoveragePlan coveragePlan);
    PersistenceResponse update(CoveragePlan coveragePlan);
    PersistenceResponse delete(Long  id);
    List<CoveragePlan> getAllCoveragePlanByPlanId(Long planId);

}
