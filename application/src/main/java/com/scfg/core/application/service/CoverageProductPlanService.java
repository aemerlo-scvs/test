package com.scfg.core.application.service;

import com.scfg.core.application.port.in.CoverageProductPlanUseCase;
import com.scfg.core.application.port.out.mortgageReliefValidations.CoverageProductPlanPort;
import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.ClfProductPlanCoverageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoverageProductPlanService implements CoverageProductPlanUseCase {

    private final CoverageProductPlanPort coverageProductPlanPort;

    @Override
    public List<ClfProductPlanCoverageDTO> getAllProductPlanCoverageByProductApsCodeAndPlanIdAndPolicyItemId(String apsCode, long planId, long policyItemId) {
        List<ClfProductPlanCoverageDTO> list = coverageProductPlanPort.findAllByApsCodeAndPlanIdAndPolicyItemId(apsCode, planId, policyItemId);
        return list;
    }

    @Override
    public List<CoverageDTO> getAllCoverageByAgreementCodePlan(Integer agreementCode) {
        return coverageProductPlanPort.findAllByAgreementCode(agreementCode);
    }

    @Override
    public List<CoverageDTO> getAllCoverageByPolicyItemId(Long policyItemId) {
        return coverageProductPlanPort.findAllByPolicyItemId(policyItemId);
    }

}
