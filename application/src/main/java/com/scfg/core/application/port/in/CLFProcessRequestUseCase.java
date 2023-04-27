package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.credicasas.ClfProcessRequestDTO;
import com.scfg.core.domain.dto.credicasas.ProcessExistRequestDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.requestDto.RequestFontDTO;

public interface CLFProcessRequestUseCase {
    ClfProcessRequestDTO processRequest(RequestFontDTO requestFontDTO);
    ClfProcessRequestDTO processSubscriptionRequest(ProcessExistRequestDTO processExistRequest);
}
