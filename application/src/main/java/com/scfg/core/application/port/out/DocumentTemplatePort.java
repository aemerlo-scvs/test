package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.DocumentTemplate;

import java.util.List;

public interface DocumentTemplatePort {
    List<DocumentTemplate> getAll();
}
