package com.scfg.core.application.port.in;

import com.scfg.core.domain.common.OperationItem;

public interface OperationItemUseCase {
    Boolean saveOrUpdate(OperationItem operationItem);
}
