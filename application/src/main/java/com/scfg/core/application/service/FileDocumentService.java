package com.scfg.core.application.service;

import com.scfg.core.application.port.in.FileDocumentUseCase;
import com.scfg.core.application.port.out.FileDocumentPort;
import com.scfg.core.application.port.out.PolicyFileDocumentPort;
import com.scfg.core.application.port.out.mortgageReliefValidations.PolicyItemPort;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.PolicyFileDocument;
import com.scfg.core.domain.PolicyItem;
import com.scfg.core.domain.dto.credicasas.FileDocumentByRequestDTO;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class FileDocumentService implements FileDocumentUseCase {
    private final FileDocumentPort fileDocumentPort;
    private final PolicyItemPort policyItemPort;
    private final PolicyFileDocumentPort policyFileDocumentPort;

    @Override
    public PolicyFileDocument registerDocument(Long requestId, FileDocumentByRequestDTO fileDocumentByRequestDTO) {
        PolicyItem policyItem = policyItemPort.getPolicyItemByGeneralRequestId(requestId);

        FileDocument file = FileDocument.builder()
                .typeDocument(fileDocumentByRequestDTO.getTypeId())
                .description(fileDocumentByRequestDTO.getDescription())
                .mime(fileDocumentByRequestDTO.getMime())
                .content(fileDocumentByRequestDTO.getContent())
                .build();

        FileDocument fileResponse = fileDocumentPort.SaveOrUpdate(file);

        PolicyFileDocument policyFileDocument = PolicyFileDocument.builder()
                .fileDocumentId(fileResponse.getId())
                .policyItemId(policyItem.getId())
                .uploadDate(DateUtils.asDate(LocalDate.now()))
                .isSigned(1)
                .build();

        PolicyFileDocument policyDocumentResponse = policyFileDocumentPort.saveOrUpdate(policyFileDocument);
        return policyDocumentResponse;
    }

    @Override
    public List<FileDocumentByRequestDTO> getDocumentsByRequestId(Long requestId) {
        return fileDocumentPort.findAllDocumentsByRequestId(requestId);
    }

    @Override
    public List<FileDocumentByRequestDTO> getSignedDocumentsByRequestId(Long requestId) {
        return fileDocumentPort.findAllSignedDocumentsByRequestId(requestId);
    }

    @Override
    public List<FileDocumentByRequestDTO> getCertificateCoverageDocumentByPolicyItemId(Long policyItemId) {
        return fileDocumentPort.getCertificateCoverageDocumentByPolicyItemId(policyItemId);
    }
}
