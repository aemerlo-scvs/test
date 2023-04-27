package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.credicasas.RequirementControlDTO;
import com.scfg.core.domain.dto.credicasas.RequirementControlGetDTO;

import java.util.List;

public interface RequirementControlPort {

    List<RequirementControlDTO> findAll();

    List<RequirementControlGetDTO> findAllByRequestId(Long requestId);

    RequirementControlDTO findById(long requirementControlId);

    List<RequirementControlDTO> findAllByFilters(String filters);

    long saveOrUpdate(RequirementControlDTO requirementControl);

    List<RequirementControlDTO> saveOrUpdateAll(List<RequirementControlDTO> requirementControlDTOS);

    long delete(Long requirementId);

    long deleteDefaultRequirement(Long requirementId);

}
