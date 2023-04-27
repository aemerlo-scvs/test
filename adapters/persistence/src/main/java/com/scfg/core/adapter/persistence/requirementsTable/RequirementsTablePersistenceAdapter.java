package com.scfg.core.adapter.persistence.requirementsTable;

import com.scfg.core.adapter.persistence.classifier.ClassifierRepository;
import com.scfg.core.adapter.persistence.product.ProductRepository;
import com.scfg.core.adapter.persistence.relationRequirementsList.RelationRequirementsListRepository;
import com.scfg.core.application.port.out.RequirementsTablePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.RequirementsTable;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class RequirementsTablePersistenceAdapter implements RequirementsTablePort {

    private final RequirementsTableRepository requirementsTableRepository;
    private final RelationRequirementsListRepository relationRequirementsListRepository;
    private final ClassifierRepository classifierRepository;
    private final ProductRepository productRepository;

    @Override
    public List<RequirementsTable> getCoverageByGeneralRequestId(long requestId) {
        List<RequirementsTableJpaEntity> requirementsTableJpaEntities = requirementsTableRepository.findAllByRequestId(requestId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return mapToDomainList(requirementsTableJpaEntities);
    }

    private List<RequirementsTable> mapToDomainList(List<RequirementsTableJpaEntity> requirementsTableJpaEntities) {
        List<RequirementsTable> requirementsTables = new ArrayList<>();
        requirementsTableJpaEntities.forEach(x -> {
            requirementsTables.add(mapToDomain(x));
        });
        return requirementsTables;
    }

    private RequirementsTable mapToDomain(RequirementsTableJpaEntity requirementsTableJpaEntity) {
        RequirementsTable requirementsTable = RequirementsTable.builder()
                .description(requirementsTableJpaEntity.getDescription())
                .finalAmount(requirementsTableJpaEntity.getFinalAmount())
                .finishAge(requirementsTableJpaEntity.getFinishAge())
                .planId(requirementsTableJpaEntity.getPlanId())
                .initialAmount(requirementsTableJpaEntity.getInitialAmount())
                .startAge(requirementsTableJpaEntity.getStartAge())
                .id(requirementsTableJpaEntity.getId())
                .createdAt(requirementsTableJpaEntity.getCreatedAt())
                .lastModifiedAt(requirementsTableJpaEntity.getLastModifiedAt())
                .build();

        return requirementsTable;
    }
}
