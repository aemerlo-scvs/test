package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.CreditOperation;

import java.util.List;
import java.util.Map;

public interface CreditOperationPort {


    List<CreditOperation> getAllCreditOperations();
    CreditOperation getCreditOperationById(long creditOperationId);
    CreditOperation getCreditOperationByNumber(long creditOperationNumber);

    PersistenceResponse save(CreditOperation creditOperation, boolean returnEntity);

    PersistenceResponse update(CreditOperation creditOperation);

    PersistenceResponse delete(CreditOperation creditOperation);

    // custom methods


    Map<String, Object> findOrUpsert(CreditOperation creditOperation) throws NoSuchFieldException, IllegalAccessException;
}
