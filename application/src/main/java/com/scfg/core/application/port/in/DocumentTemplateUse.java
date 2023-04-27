package com.scfg.core.application.port.in;

import com.scfg.core.domain.common.DocumentTemplate;

import java.util.List;

public interface DocumentTemplateUse {
    List<DocumentTemplate> getDocumentTemplateList(String reporting);
}
