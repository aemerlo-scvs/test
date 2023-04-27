package com.scfg.core.application.port.out;

import com.scfg.core.domain.Coverage;

import java.util.List;

public interface CoveragePort {

    List<Coverage> getAllCoverage();

    String getAllCoverageNamesByGeneralRequestId(Long requestId);
}
