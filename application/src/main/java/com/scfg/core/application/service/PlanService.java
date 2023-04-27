package com.scfg.core.application.service;

import com.scfg.core.application.port.in.PlanUseCase;
import com.scfg.core.application.port.out.PlanPort;
import com.scfg.core.application.port.out.ProductPort;
import com.scfg.core.domain.Plan;
import com.scfg.core.domain.dto.credicasas.ClfPlanDTO;
import com.scfg.core.domain.dto.credicasas.PlanInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService implements PlanUseCase {

    private final PlanPort planPort;

    @Override
    public List<ClfPlanDTO> getAllPlans() {
        return planPort.clfFindAll();
    }

    @Override
    public List<Plan> getAllPlansByBusinessGroupId(Integer businessGroupIdc) {
        return planPort.getPlanByFinancialGroup(businessGroupIdc);
    }

    @Override
    public Plan getPlanByAgreementCode(Integer agreementCode) {
        return planPort.findPlanByAgreementCode(agreementCode);
    }

    @Override
    public List<Plan> findPlanByProductID(Long productId) {
        return planPort.getPlanByProductId(productId);
    }

    @Override
    public List<PlanInformation> findPlanByRequestId(List<Long> requestList) {
        return planPort.getPlanByRequestId(requestList);
    }
}
