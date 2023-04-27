package com.scfg.core.application.service;

import com.scfg.core.application.port.in.DocumentTemplateUse;
import com.scfg.core.application.port.out.DocumentTemplatePort;
import com.scfg.core.domain.common.DocumentTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentTemplateService implements DocumentTemplateUse {
    private final DocumentTemplatePort documentTemplatePort;

    @Override
    public List<DocumentTemplate> getDocumentTemplateList(String reporting) {
        List<DocumentTemplate> list = documentTemplatePort.getAll();
        List<DocumentTemplate> templateList = new ArrayList<>();
        DocumentTemplate template = (DocumentTemplate) list.stream().filter(a -> a.getDescription().equals(reporting)).findFirst().get();
        templateList.add(template);
        List<DocumentTemplate> templates = list.stream().filter(a -> a.getIdDocumentTemplate() == template.getId()).collect(Collectors.toList());
        if (templates.size() > 0)
            templateList.addAll(templates);
        return templateList;
    }
}
