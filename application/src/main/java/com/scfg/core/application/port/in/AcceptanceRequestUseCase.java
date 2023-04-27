package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.MessageDecideResponseDTO;
import com.scfg.core.domain.dto.vin.VinProposalDetail;

public interface AcceptanceRequestUseCase {
    Boolean acceptanceRequest(MessageDecideResponseDTO messageResponseDTO);
    VinProposalDetail getProposalDetail(Long requestId);
}
