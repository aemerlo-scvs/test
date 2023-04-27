package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.OperationHeader;

public interface OperationHeaderPort {
    OperationHeader findByGeneralRequestId(Long generalRequestId);
}
