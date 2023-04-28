package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.vin.AnnexeRequirementDto;
import com.scfg.core.domain.common.AnnexeRequirementControl;

import java.util.List;

public interface AnnexeRequirementControlPort {

    AnnexeRequirementControl findByRequestAnnexeIdAndFileDocumentId(Long requestAnnexeId, Long fileDocumentId);
    List<AnnexeRequirementDto> findAllByRequestAnnexeIdAndAnnexeTypeId(Long requestAnnexeId);
    AnnexeRequirementControl getAnnexeRequirementControlByIdOrExcepcion(Long annexeRequirementControlId);
    Long saveOrUpdate(AnnexeRequirementControl annexeRequirementControl);
}
