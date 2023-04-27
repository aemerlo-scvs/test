package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.credicasas.RegisterRequirementControlDTO;
import com.scfg.core.domain.dto.credicasas.RequirementControlGetDTO;

import java.util.List;

public interface RequirementControlUseCase {

    List<RequirementControlGetDTO> getAllRequirementsById(Long requestId);

    Long addRequirement(RegisterRequirementControlDTO registerRequirementControlDTO);

    Long editRequirement(RegisterRequirementControlDTO registerRequirementControlDTO);

    Long deleteRequirement(RegisterRequirementControlDTO requirementControlDTO);
}
