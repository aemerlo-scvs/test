package com.scfg.core.application.port.out;

import com.scfg.core.domain.Document;

import java.util.List;

public interface AnnexeFileDocumentPort {
    List<Document> findAll();
    Long saveOrUpdate(Document document);
    Document getFileOptionalOrException(Long documentId);
    Document getFileByRequestAnnexeIdAndTypeDocumentIdc(Long requestAnnexeId, Integer typeDocumentIdc);
    Document findRequestAnnexeIdAndAnnexeTypeIdcAndSigned(Long requestAnnexeId, Integer typeDocumentIdc, Boolean signed);
    Long getNextNumber(Integer documentTypeIdc);

}

