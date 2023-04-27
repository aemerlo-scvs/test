package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.vin.RequirementDTO;

public interface AnnexeRequirementControlPort {
    Long saveOrUpdate(RequirementDTO requirement, Long fileDocumentId, Long requestAnnexeId);

}
