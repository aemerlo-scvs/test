package com.scfg.core.application.port.out;

import com.scfg.core.domain.RelationRequirements;

import java.util.List;

public interface RelationRequirementsListPort {

    List<RelationRequirements> getAllRelationRequirementListByRequirementTableId(long requirementTableId);
}
