package com.scfg.core.application.service;

import com.scfg.core.application.port.in.CoveragePlanUseCase;
import com.scfg.core.application.port.out.CoveragePlanPort;
import com.scfg.core.common.UseCase;
import com.scfg.core.domain.CoveragePlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@UseCase
@Slf4j
@RequiredArgsConstructor
public class CoveragePlanService implements CoveragePlanUseCase {
    private final CoveragePlanPort coveragePlanPort;
    @Override
    public List<CoveragePlan> getAllCoveragePlanByPlanId(Long planId) {
        return coveragePlanPort.getAllCoveragePlanByPlanId(planId);
    }
}
