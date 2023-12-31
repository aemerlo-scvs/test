package com.scfg.core.application.port.in;

import com.scfg.core.domain.Plan;
import com.scfg.core.domain.dto.credicasas.ClfPlanDTO;
import com.scfg.core.domain.dto.credicasas.PlanInformation;

import java.util.List;

public interface PlanUseCase {
    List<ClfPlanDTO> getAllPlans();
    List<Plan> findPlanByProductID(Long productId);
    List<PlanInformation> findPlanByRequestId(List<Long> requestList);

    List<Plan> getAllPlansByBusinessGroupId(Integer businessGroupIdc);
    Plan getPlanByAgreementCode(Integer agreementCode);
}
