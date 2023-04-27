package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.AnnexeRequirement;

import java.util.List;

public interface AnnexeRequirementPort {
    List<AnnexeRequirement> findAllByAnnexeTypeId(Long annexeTypeId);
    List<AnnexeRequirement> findAll();
}
