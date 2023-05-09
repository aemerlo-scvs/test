package com.scfg.core.application.port.out;

import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.credicasas.FileDocumentByRequestDTO;

import java.util.List;

public interface FileDocumentPort {
    List<FileDocument> findAll();
    FileDocument findById(Long fileId);
    FileDocument SaveOrUpdate(FileDocument fileDocument);
    List<FileDocumentDTO> SaveOrUpdateAll(List<FileDocumentDTO> fileDocument);
    List<FileDocumentByRequestDTO> findAllDocumentsByRequestId(Long requestId);
    List<FileDocumentByRequestDTO> findAllSignedDocumentsByRequestId(Long requestId);
    long deleteFileDocument(Long fileDocumentId);
    List<FileDocumentByRequestDTO> getCertificateCoverageDocumentByPolicyItemId(Long policyItemId);
}
