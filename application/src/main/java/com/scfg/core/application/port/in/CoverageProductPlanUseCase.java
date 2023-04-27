package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.ClfProductPlanCoverageDTO;

import java.util.List;

public interface CoverageProductPlanUseCase {

    List<ClfProductPlanCoverageDTO> getAllProductPlanCoverageByProductApsCodeAndPlanIdAndPolicyItemId(String apsCode, long planId, long policyItemId);

    List<CoverageDTO> getAllCoverageByAgreementCodePlan(Integer agreementCode);
    List<CoverageDTO> getAllCoverageByPolicyItemId(Long policyItemId);
}
