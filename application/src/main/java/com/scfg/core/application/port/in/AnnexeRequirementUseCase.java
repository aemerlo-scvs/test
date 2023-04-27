package com.scfg.core.application.port.in;

import com.scfg.core.domain.common.AnnexeRequirement;

import java.util.List;

public interface AnnexeRequirementUseCase {
    List<AnnexeRequirement> getAllByAnnexeTypeId(Long annexeTypeId);

}
