package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.credicasas.RequestDetailDTO;

public interface RequestDetailUseCase {
    RequestDetailDTO getRequestDetail(RequestDetailDTO requestDetailDTO);
}
