package com.scfg.core.application.service;

import com.scfg.core.application.port.in.RequirementControlUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.application.port.out.mortgageReliefValidations.PolicyItemPort;
import com.scfg.core.common.UseCase;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.PolicyItem;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.credicasas.RegisterRequirementControlDTO;
import com.scfg.core.domain.dto.credicasas.RequirementControlDTO;
import com.scfg.core.domain.dto.credicasas.RequirementControlGetDTO;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@UseCase
@RequiredArgsConstructor
public class RequirementControlService implements RequirementControlUseCase {

    private final RequirementControlPort requirementControlPort;
    private final FileDocumentPort fileDocumentPort;
    private final PolicyItemPort policyItemPort;

    @Override
    public List<RequirementControlGetDTO> getAllRequirementsById(Long requestId) {
        List<RequirementControlGetDTO> requirementControlDTOList = requirementControlPort
                .findAllByRequestId(requestId);
        requirementControlDTOList.forEach(requirementControlDTO -> {
            if (requirementControlDTO.getFileDocument().getId() != null) {
                FileDocument fd = fileDocumentPort.findById(requirementControlDTO.getFileDocument().getId());
                if (fd != null) {
                    FileDocumentDTO fdd = FileDocumentDTO.builder()
                            .id(fd.getId())
                            .content(fd.getContent())
                            .mime(fd.getMime())
                            .name(fd.getDescription())
                            .path(fd.getDirectoryLocation())
                            .build();
                    requirementControlDTO.setFileDocument(fdd);
                } else {
                    requirementControlDTO.setFileDocument(null);
                }
            }
        });
        return requirementControlDTOList;
    }

    @Override
    public Long addRequirement(RegisterRequirementControlDTO registerRequirementControlDTO) {
        if (registerRequirementControlDTO.getFileDocument() != null) {
            FileDocument file = FileDocument.builder()
                    .description(registerRequirementControlDTO.getDescription())
                    .content(registerRequirementControlDTO.getFileDocument().getContent())
                    .mime(registerRequirementControlDTO.getFileDocument().getMime())
                    .build();
            FileDocument fileResponse = fileDocumentPort.SaveOrUpdate(file);

            PolicyItem policyItem = policyItemPort.getPolicyItemByGeneralRequestId(registerRequirementControlDTO.getRequestId());

            RequirementControlDTO requirementControlDTO = RequirementControlDTO.builder()
                    .description(registerRequirementControlDTO.getDescription())
                    .comment(registerRequirementControlDTO.getComment())
                    .requestDate(LocalDate.now())
                    .receptionDate(registerRequirementControlDTO.getReceptionDate())
                    .fileDocument(fileResponse)
                    .policyItemId(policyItem.getId())
                    .requirementId(registerRequirementControlDTO.getRequirementId())
                    .build();
            Long id = requirementControlPort.saveOrUpdate(requirementControlDTO);
            return id;
        } else {
            PolicyItem policyItem = policyItemPort.getPolicyItemByGeneralRequestId(registerRequirementControlDTO.getRequestId());

            RequirementControlDTO requirementControlDTO = RequirementControlDTO.builder()
                    .description(registerRequirementControlDTO.getDescription())
                    .comment(registerRequirementControlDTO.getComment())
                    .requestDate(LocalDate.now())
                    .receptionDate(registerRequirementControlDTO.getReceptionDate())
                    .policyItemId(policyItem.getId())
                    .requirementId(registerRequirementControlDTO.getRequirementId())
                    .build();
            Long id = requirementControlPort.saveOrUpdate(requirementControlDTO);
            return id;
        }
    }

    @Override
    public Long editRequirement(RegisterRequirementControlDTO registerRequirementControlDTO) {
        if (registerRequirementControlDTO.getFileDocument() != null) {
            if (registerRequirementControlDTO.getFileDocument().getId() != null) {
                FileDocument file = FileDocument.builder()
                        .id(registerRequirementControlDTO.getFileDocument().getId())
                        .description(registerRequirementControlDTO.getDescription())
                        .content(registerRequirementControlDTO.getFileDocument().getContent())
                        .mime(registerRequirementControlDTO.getFileDocument().getMime())
                        .build();
                FileDocument fileResponse = fileDocumentPort.SaveOrUpdate(file);

                RequirementControlDTO requirementControlDTO = RequirementControlDTO.builder()
                        .id(registerRequirementControlDTO.getId())
                        .description(registerRequirementControlDTO.getDescription())
                        .comment(registerRequirementControlDTO.getComment())
                        .requestDate(registerRequirementControlDTO.getRequestDate())
                        .receptionDate(registerRequirementControlDTO.getReceptionDate())
                        .fileDocument(fileResponse)
                        .policyItemId(registerRequirementControlDTO.getPolicyItemId())
                        .requirementId(registerRequirementControlDTO.getRequirementId())
                        .build();
                Long id = requirementControlPort.saveOrUpdate(requirementControlDTO);
                return id;
            } else {
                FileDocument file = FileDocument.builder()
                        .description(registerRequirementControlDTO.getDescription())
                        .content(registerRequirementControlDTO.getFileDocument().getContent())
                        .mime(registerRequirementControlDTO.getFileDocument().getMime())
                        .build();
                FileDocument fileResponse = fileDocumentPort.SaveOrUpdate(file);

                RequirementControlDTO requirementControlDTO = RequirementControlDTO.builder()
                        .id(registerRequirementControlDTO.getId())
                        .description(registerRequirementControlDTO.getDescription())
                        .comment(registerRequirementControlDTO.getComment())
                        .requestDate(registerRequirementControlDTO.getRequestDate())
                        .receptionDate(registerRequirementControlDTO.getReceptionDate())
                        .fileDocument(fileResponse)
                        .policyItemId(registerRequirementControlDTO.getPolicyItemId())
                        .requirementId(registerRequirementControlDTO.getRequirementId())
                        .build();
                Long id = requirementControlPort.saveOrUpdate(requirementControlDTO);
                return id;
            }

        } else {
            RequirementControlDTO requirementControlDTO = RequirementControlDTO.builder()
                    .id(registerRequirementControlDTO.getId())
                    .description(registerRequirementControlDTO.getDescription())
                    .comment(registerRequirementControlDTO.getComment())
                    .requestDate(LocalDate.now())
                    .receptionDate(registerRequirementControlDTO.getReceptionDate())
                    .policyItemId(registerRequirementControlDTO.getPolicyItemId())
                    .requirementId(registerRequirementControlDTO.getRequirementId())
                    .build();
            Long id = requirementControlPort.saveOrUpdate(requirementControlDTO);
            return id;

        }
    }

    @Override
    public Long deleteRequirement(RegisterRequirementControlDTO registerRequirementControlDTO) {
        Long requirementIdDeleted = 0L;
        if (registerRequirementControlDTO.getId() != null && (registerRequirementControlDTO.getRequirementId() == null ||
                registerRequirementControlDTO.getRequirementId() == 0)) {
            requirementIdDeleted = requirementControlPort.delete(registerRequirementControlDTO.getId());
        }
        if (registerRequirementControlDTO.getId() != null && (registerRequirementControlDTO.getRequirementId() != null &&
                registerRequirementControlDTO.getRequirementId() != 0)) {
            requirementIdDeleted = requirementControlPort.deleteDefaultRequirement(registerRequirementControlDTO.getId());
        }
        if (registerRequirementControlDTO.getFileDocument().getId() != null) {
            fileDocumentPort.deleteFileDocument(registerRequirementControlDTO.getFileDocument().getId());
        }
        return requirementIdDeleted;
    }
}
