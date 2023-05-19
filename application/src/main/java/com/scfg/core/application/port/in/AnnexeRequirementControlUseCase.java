package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.vin.AnnexeRequirementDto;

import java.util.List;

public interface AnnexeRequirementControlUseCase {
    List<AnnexeRequirementDto> getAllByRequestAnnexeIdAndAnnexeTypeId(Long requestAnnexeId, Long annexeTypeId);

}
