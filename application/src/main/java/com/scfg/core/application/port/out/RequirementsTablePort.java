package com.scfg.core.application.port.out;

import com.scfg.core.domain.RequirementsTable;

import java.util.List;

public interface RequirementsTablePort {

    List<RequirementsTable> getCoverageByGeneralRequestId(long requestId);
}
