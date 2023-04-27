package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.credicasas.groupthefont.ClfProductPlanCoverageDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.ClfSaveCoverageDTO;

import java.util.List;

public interface CoveragePolicyItemUseCase {
    Boolean saveOrUpdateAll(ClfSaveCoverageDTO oSaveCoverage);
}
