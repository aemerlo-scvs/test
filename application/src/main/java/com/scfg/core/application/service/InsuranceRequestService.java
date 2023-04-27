package com.scfg.core.application.service;

import com.scfg.core.application.port.in.InsuranceRequestUseCase;
import com.scfg.core.application.port.out.InsuranceRequestPort;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.InsuranceRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;


@UseCase
@RequiredArgsConstructor
public class InsuranceRequestService implements InsuranceRequestUseCase {

    private final InsuranceRequestPort insuranceRequestPort;

    @Override
    public List<InsuranceRequest> getAllInsuranceRequests() {
        return insuranceRequestPort.getAllInsuranceRequests();
    }

    @Override
    public PersistenceResponse registerInsuranceRequest(InsuranceRequest insuranceRequest) {
        return insuranceRequestPort.save(insuranceRequest);
    }
}
