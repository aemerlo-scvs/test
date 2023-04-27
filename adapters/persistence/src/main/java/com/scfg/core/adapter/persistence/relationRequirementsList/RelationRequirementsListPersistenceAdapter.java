package com.scfg.core.adapter.persistence.relationRequirementsList;

import com.scfg.core.application.port.out.RelationRequirementsListPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.RelationRequirements;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class RelationRequirementsListPersistenceAdapter implements RelationRequirementsListPort {

    private final RelationRequirementsListRepository relationRequirementsListRepository;

    @Override
    public List<RelationRequirements> getAllRelationRequirementListByRequirementTableId(long requirementTableId) {
        List<RelationRequirementsListJpaEntity> relationRequirementsListJpaEntities = relationRequirementsListRepository.findAllByRequirementTableId(requirementTableId);
        return mapToDomainList(relationRequirementsListJpaEntities);
    }

    private List<RelationRequirements> mapToDomainList(List<RelationRequirementsListJpaEntity> o) {
        List<RelationRequirements> relationRequirements = new ArrayList<>();
        o.forEach(x -> {
            relationRequirements.add(mapToDomain(x));
        });
        return relationRequirements;
    }

    private RelationRequirements mapToDomain(RelationRequirementsListJpaEntity x) {
        RelationRequirements relationRequirements = RelationRequirements.builder()
                .id(x.getId())
                .requirementIdc(x.getRequirementIdc())
                .requirementTableId(x.getRequirementTableId())
                .createdAt(x.getCreatedAt())
                .lastModifiedAt(x.getLastModifiedAt())
                .build();

        return relationRequirements;
    }
}
