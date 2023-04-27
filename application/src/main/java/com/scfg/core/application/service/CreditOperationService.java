package com.scfg.core.application.service;

import com.scfg.core.application.port.in.CreditOperationUseCase;
import com.scfg.core.application.port.out.CreditOperationPort;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.CreditOperation;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class CreditOperationService implements CreditOperationUseCase {

    private final CreditOperationPort creditOperationPort;

    @Override
    public List<CreditOperation> getAllCreditsOperations() {
        return creditOperationPort.getAllCreditOperations();
    }

    @Override
    public PersistenceResponse registerCreditOperation(CreditOperation creditOperation) {
        return creditOperationPort.save(creditOperation, true);
    }
}
