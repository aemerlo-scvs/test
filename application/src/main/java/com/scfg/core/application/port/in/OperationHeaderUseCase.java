package com.scfg.core.application.port.in;

import com.scfg.core.domain.common.OperationHeader;

public interface OperationHeaderUseCase {

    OperationHeader getByGeneralRequestId(Long generalRequestId);
}
