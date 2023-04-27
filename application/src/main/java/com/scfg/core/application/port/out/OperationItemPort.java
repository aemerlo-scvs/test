package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.OperationItem;

public interface OperationItemPort {
    Long saveOrUpdate(OperationItem operationItem);

    OperationItem findByOperationHeaderIdAndMonthAndYear(Long operationHeaderId, Integer month, Integer year);
}
