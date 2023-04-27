package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.PolicyFileDocument;
import com.scfg.core.domain.dto.credicasas.FileDocumentByRequestDTO;

import java.util.List;

public interface FileDocumentUseCase {
    PolicyFileDocument registerDocument(Long requestId, FileDocumentByRequestDTO fileDocumentByRequestDTO);
    List<FileDocumentByRequestDTO> getDocumentsByRequestId(Long requestId);
    List<FileDocumentByRequestDTO> getSignedDocumentsByRequestId(Long requestId);
    List<FileDocumentByRequestDTO> getCertificateCoverageDocumentByPolicyItemId(Long policyItemId);
}
