package com.scfg.core.application.service;

import com.scfg.core.application.port.in.OperationItemUseCase;
import com.scfg.core.application.port.out.OperationItemPort;
import com.scfg.core.domain.common.OperationItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationItemService implements OperationItemUseCase {

    private final OperationItemPort operationItemPort;

    @Override
    public Boolean saveOrUpdate(OperationItem operationItem) {
        OperationItem operationItemAux = operationItemPort.findByOperationHeaderIdAndMonthAndYear(operationItem.getOperationHeaderId(), operationItem.getMonthIdc(), operationItem.getYearIdc());
        if (operationItemAux != null) {
            operationItem.setId(operationItemAux.getId());
        }
        return operationItemPort.saveOrUpdate(operationItem) > 0;
    }
}
