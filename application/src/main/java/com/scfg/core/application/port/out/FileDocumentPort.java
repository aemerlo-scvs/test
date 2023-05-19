package com.scfg.core.application.port.out;

import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.credicasas.FileDocumentByRequestDTO;

import java.util.List;

public interface FileDocumentPort {
    List<FileDocument> findAll();
    List<FileDocumentByRequestDTO> findAllDocumentsByRequestId(Long requestId);
    List<FileDocumentByRequestDTO> findAllSignedDocumentsByRequestId(Long requestId);
    FileDocument findLastByPolicyItemIdAndDocumentTypeIdc(Long policyItemId, Integer documentTypeIdc);
    FileDocument findById(Long fileId);
    List<FileDocumentByRequestDTO> getCertificateCoverageDocumentByPolicyItemId(Long policyItemId);
    FileDocument SaveOrUpdate(FileDocument fileDocument);
    List<FileDocumentDTO> SaveOrUpdateAll(List<FileDocumentDTO> fileDocument);
    long deleteFileDocument(Long fileDocumentId);

}
