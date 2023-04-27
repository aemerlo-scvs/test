package com.scfg.core.application.service;

import com.scfg.core.application.port.in.OperationHeaderUseCase;
import com.scfg.core.application.port.out.OperationHeaderPort;
import com.scfg.core.domain.common.OperationHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationHeaderService implements OperationHeaderUseCase {

    private final OperationHeaderPort operationHeaderPort;

    @Override
    public OperationHeader getByGeneralRequestId(Long generalRequestId) {
        return operationHeaderPort.findByGeneralRequestId(generalRequestId);
    }
}
