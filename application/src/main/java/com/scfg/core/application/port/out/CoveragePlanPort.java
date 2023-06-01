package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CoveragePlan;

import java.util.List;

public interface CoveragePlanPort {
    PersistenceResponse saveOrUpdate(CoveragePlan coveragePlan);
    PersistenceResponse delete(Long  id);
    CoveragePlan getCoveragePlanByPlanIdAndCoverageId(Long planId, Long coverageId);
    List<CoveragePlan> getAllCoveragePlan(); //TODO este no me sirve
    List<CoveragePlan> getAllCoveragePlanByPlanId(Long planId);

}
