package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.vin.RequestAnnexeDTO;

public interface RequestAnnexeUseCase {
    Boolean processRequest(RequestAnnexeDTO requestAnnexeDTO);
    Boolean hasPendingRequests(Long policyId, Long annexeTypeId);
}
