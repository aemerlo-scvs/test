package com.scfg.core.application.port.out;

import com.scfg.core.domain.Plan;
import com.scfg.core.domain.dto.credicasas.ClfPlanDTO;
import com.scfg.core.domain.dto.credicasas.PlanInformation;

import java.util.List;

public interface PlanPort {

    List<Plan> getPlanList();

    List<ClfPlanDTO> clfFindAll();
    List<ClfPlanDTO> clfFindAllByBusinessGroupIdc();

    Plan getPlanById(Long planId);

    List<Plan> getPlanByFinancialGroup(Integer financialGroupIdc);

    List<Plan> getPlanByProductId(Long productId);
    List<PlanInformation> getPlanByRequestId(List<Long> requestList);

    Plan getPlanByAgreementCodePlandAndAgreementCodeProduct(Integer agreementCodePlan, Integer agreementCodeProduct);

    Plan findPlanByAgreementCode(Integer agreementCode);
}
