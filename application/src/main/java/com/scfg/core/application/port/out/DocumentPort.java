package com.scfg.core.application.port.out;

import com.scfg.core.domain.Document;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.dto.FileDocumentDTO;

import java.security.Policy;
import java.util.List;

public interface DocumentPort {
    List<Document> getAllDocument();
    Boolean saveOrUpdate(Document document);
    Boolean saveOrUpdate(FileDocumentDTO file, long personId);
}
