package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.InsuranceRequest;
import com.scfg.core.domain.dto.FileDocumentDTO;

import java.util.List;
import java.util.Map;

public interface InsuranceRequestPort {


    List<InsuranceRequest> getAllInsuranceRequests();
    InsuranceRequest getInsuranceRequestById(long insuranceRequestId);
    InsuranceRequest getInsuranceRequestByNumber(String insuranceRequestNumber);

    PersistenceResponse save(InsuranceRequest insuranceRequest);

    PersistenceResponse update(InsuranceRequest insuranceRequest);

    PersistenceResponse delete(InsuranceRequest insuranceRequest);

    Map<String, Object> findOrUpsert(InsuranceRequest insuranceRequest) throws NoSuchFieldException, IllegalAccessException;
}
