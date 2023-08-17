package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.DocumentTemplate;
import com.scfg.core.domain.dto.FileDocumentDTO;

import java.io.FileNotFoundException;
import java.util.List;

public interface DocumentTemplateUse {
    List<DocumentTemplate> getDocumentTemplateList(String reporting);
    boolean save(DocumentTemplate documentTemplate);
    FileDocumentDTO load() throws FileNotFoundException;

}
