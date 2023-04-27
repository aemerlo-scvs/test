package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.CoverageProductPlan;
import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.dto.credicasas.CoverageClf;
import com.scfg.core.domain.dto.credicasas.groupthefont.ClfProductPlanCoverageDTO;

import java.util.List;

public interface CoverageProductPlanPort {

    CoverageProductPlan findById(Long id);

    CoverageProductPlan findCoverageProductPlanByPlanIdAndProductId(Long productId,Long planId);

    List<ClfProductPlanCoverageDTO> findAllByApsCodeAndPlanIdAndPolicyItemId(String apsCode, long planId, long policyItemId);

    List<CoverageClf> getAllCoverageClf(Long generalRequest);

    List<CoverageDTO> findAllByAgreementCode(Integer agreementCode);
    List<CoverageDTO> findAllByPolicyItemId(Long policyItemId);
}
