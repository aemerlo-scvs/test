package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.credicasas.RequestDetailDTO;

public interface RequestDetailPort {

    RequestDetailDTO getRequestDetailById(Long requestId);

    String getJuridicalNameByPolicyItemId(Long policyItemId);

}
