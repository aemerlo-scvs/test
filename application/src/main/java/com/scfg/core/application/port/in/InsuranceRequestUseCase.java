package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.InsuranceRequest;

import java.util.List;

public interface InsuranceRequestUseCase {
    List<InsuranceRequest> getAllInsuranceRequests();
    PersistenceResponse registerInsuranceRequest(InsuranceRequest insuranceRequest);
}
