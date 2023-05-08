package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Coverage;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;

import java.util.List;

public interface CoveragePort {

    List<Coverage> getAllCoverage();

    String getAllCoverageNamesByGeneralRequestId(Long requestId);
    PersistenceResponse save(Coverage coverage, boolean returnEntity);
    PersistenceResponse update(Coverage coverage);
    PersistenceResponse delete(Long  id);



    List<Coverage> getfilterParamenters(FilterParamenter paramenter);
    List<Coverage> findAllCoverageByProductId(Long productId);
}
