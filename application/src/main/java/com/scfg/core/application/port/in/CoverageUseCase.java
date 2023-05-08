package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Coverage;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;

import java.util.List;

public interface CoverageUseCase {
    List<Coverage> getAllCoverage();

    PersistenceResponse registerCoverage(Coverage coverage);

    PersistenceResponse updateCoverage(Coverage coverage);

    PersistenceResponse deleteCoverage(Long id);

    List<Coverage> getfilterParamenter(FilterParamenter paramenter);

    List<Coverage> getAllCoverageByProductId(Long productId);

 //   List<Coverage> findByBranchId(Long branchId);


}
