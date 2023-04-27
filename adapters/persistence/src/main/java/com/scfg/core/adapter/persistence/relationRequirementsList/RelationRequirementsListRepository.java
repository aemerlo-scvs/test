package com.scfg.core.adapter.persistence.relationRequirementsList;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationRequirementsListRepository extends JpaRepository<RelationRequirementsListJpaEntity, Long> {
    List<RelationRequirementsListJpaEntity> findAllByRequirementTableId(long requirementTableId);
}
