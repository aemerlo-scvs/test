package com.scfg.core.adapter.persistence.requirementControl;

import com.scfg.core.application.port.out.RequirementControlPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.credicasas.RequirementControlDTO;
import com.scfg.core.domain.dto.credicasas.RequirementControlGetDTO;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class RequirementControlAdapter implements RequirementControlPort {

    private final RequirementControlRepository requirementControlRepository;

    @Override
    public List<RequirementControlDTO> findAll() {
        return null;
    }

    @Override
    public List<RequirementControlGetDTO> findAllByRequestId(Long requestId) {
        List<RequirementControlJpaEntity> list = requirementControlRepository.findAllByRequestId(requestId);
        List<RequirementControlGetDTO> requirementControlDTOList = list.stream()
                .map(this::mapToDomainGet)
                .collect(Collectors.toList());
        return requirementControlDTOList;
    }

    @Override
    public RequirementControlDTO findById(long requirementControlId) {
        return null;
    }

    @Override
    public List<RequirementControlDTO> findAllByFilters(String filters) {
        return null;
    }

    @Override
    public long saveOrUpdate(RequirementControlDTO requirementControl) {
        RequirementControlJpaEntity requirementControlJpaEntity = mapToJpaEntity(requirementControl);
        requirementControlJpaEntity = requirementControlRepository.save(requirementControlJpaEntity);
        return  requirementControlJpaEntity.getId();
    }

    @Override
    public List<RequirementControlDTO> saveOrUpdateAll(List<RequirementControlDTO> requirementControlDTOS) {
        List<RequirementControlJpaEntity> requirementControlJpaEntities = mapToListEntity(requirementControlDTOS);
        requirementControlJpaEntities = requirementControlRepository.saveAll(requirementControlJpaEntities);
        return mapToListDomain(requirementControlJpaEntities);
    }

    @Override
    public long delete(Long requirementId) {
        RequirementControlJpaEntity requirementControlJpaEntity = requirementControlRepository.findById(requirementId).orElse(null);
        requirementControlJpaEntity.setStatus(PersistenceStatusEnum.DELETED.getValue());
        return requirementControlRepository.save(requirementControlJpaEntity).getId();
    }

    @Override
    public long deleteDefaultRequirement(Long requirementId) {
        RequirementControlJpaEntity requirementControlJpaEntity = requirementControlRepository.findById(requirementId).orElse(null);
        requirementControlJpaEntity.setFileDocumentId(null);
        requirementControlJpaEntity.setReceptionDate(null);
        return requirementControlRepository.save(requirementControlJpaEntity).getId();
    }

    //#region Mappers

    public static RequirementControlJpaEntity mapToJpaEntity(RequirementControlDTO requirementControlDTO) {
        RequirementControlJpaEntity requirementControlJpaEntity = RequirementControlJpaEntity.builder()
                .id(requirementControlDTO.getId())
                .description(requirementControlDTO.getDescription())
                .comment(requirementControlDTO.getComment())
                .requestDate(requirementControlDTO.getRequestDate())
                .receptionDate(requirementControlDTO.getReceptionDate())
                .fileDocumentId(requirementControlDTO.getFileDocument() != null ? requirementControlDTO.getFileDocument().getId() : null)
                .requirementId(requirementControlDTO.getRequirementId())
                .policyItemId(requirementControlDTO.getPolicyItemId())
                .build();

        return requirementControlJpaEntity;
    }

    private RequirementControlDTO mapToDomain(RequirementControlJpaEntity requirementControlJpaEntity) {
        RequirementControlDTO requirementControlDTO = RequirementControlDTO.builder()
                .id(requirementControlJpaEntity.getId())
                .comment(requirementControlJpaEntity.getComment())
                .description(requirementControlJpaEntity.getDescription())
                .requestDate(requirementControlJpaEntity.getRequestDate())
                .receptionDate(requirementControlJpaEntity.getReceptionDate())
                .requirementId(requirementControlJpaEntity.getRequirementId())
                .fileDocument(FileDocument.builder()
                        .id(requirementControlJpaEntity.getId())
                        .build())
                .policyItemId(requirementControlJpaEntity.getPolicyItemId())
                .build();

        return requirementControlDTO;
    }

    private RequirementControlGetDTO mapToDomainGet(RequirementControlJpaEntity requirementControlJpaEntity) {
        RequirementControlGetDTO requirementControlDTO = RequirementControlGetDTO.builder()
                .id(requirementControlJpaEntity.getId())
                .comment(requirementControlJpaEntity.getComment())
                .description(requirementControlJpaEntity.getDescription())
                .requestDate(requirementControlJpaEntity.getRequestDate())
                .receptionDate(requirementControlJpaEntity.getReceptionDate())
                .requirementId(requirementControlJpaEntity.getRequirementId())
                .fileDocument(FileDocumentDTO.builder()
                        .id(requirementControlJpaEntity.getFileDocumentId())
                        .build())
                .policyItemId(requirementControlJpaEntity.getPolicyItemId())
                .build();

        return requirementControlDTO;
    }

    private List<RequirementControlJpaEntity> mapToListEntity(List<RequirementControlDTO> requirementControlDTOS){
        List<RequirementControlJpaEntity> requirementControlJpaEntities = new ArrayList<>();
        requirementControlDTOS.forEach(o -> {
            requirementControlJpaEntities.add(mapToJpaEntity(o));
        });
        return requirementControlJpaEntities;
    }

    private List<RequirementControlDTO> mapToListDomain(List<RequirementControlJpaEntity> requirementControlDTOS){
        List<RequirementControlDTO> requirementControlJpaEntities = new ArrayList<>();
        requirementControlDTOS.forEach(o -> {
            requirementControlJpaEntities.add(mapToDomain(o));
        });
        return requirementControlJpaEntities;
    }

    //#endregion
}
