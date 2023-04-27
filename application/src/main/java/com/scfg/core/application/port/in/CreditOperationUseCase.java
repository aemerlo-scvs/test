package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.CreditOperation;

import java.util.List;

public interface CreditOperationUseCase {

    List<CreditOperation> getAllCreditsOperations();

    PersistenceResponse registerCreditOperation(CreditOperation creditOperation);

}
