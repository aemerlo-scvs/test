package com.scfg.core.adapter.persistence.documentTemplate;

import com.scfg.core.adapter.persistence.document.DocumentJpaEntity;
import com.scfg.core.adapter.persistence.observedCase.ObservedCaseJpaEntity;
import com.scfg.core.application.port.out.DocumentTemplatePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.util.ObjectMapperUtils;
import com.scfg.core.domain.common.DocumentTemplate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;


@PersistenceAdapter
@RequiredArgsConstructor
public class DocumentTemplateAdapter implements DocumentTemplatePort {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTemplateAdapter.class);
    private final DocumentTemplateRepository documentTemplateRepository;

    @Override
    public List<DocumentTemplate> getAll() {
        try {
            List<DocumentTemplate> documentTemplates = new ArrayList<>();
            List<DocumentTemplateJpaEntity> obj = documentTemplateRepository.findAll();

            obj.forEach(documentTemplateJpaEntity -> {
                documentTemplates.add(mapToDomain(documentTemplateJpaEntity));
            });
            return documentTemplates;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return null;
        }

    }

    @Override
    public List<DocumentTemplate> getDocumentByProductId(Long productId) {
        List< DocumentTemplateJpaEntity>  documentJpaEntity=documentTemplateRepository.findByProductId(productId);
        return  ObjectMapperUtils.mapAll(documentJpaEntity,DocumentTemplate.class);
    }

    @Override
    public boolean save(DocumentTemplate documentTemplate) {
        DocumentTemplateJpaEntity documentTemplateJpaEntity= ObjectMapperUtils.map(documentTemplate, DocumentTemplateJpaEntity.class);
                documentTemplateJpaEntity =documentTemplateRepository.save(documentTemplateJpaEntity);
        return documentTemplateJpaEntity.getId()!=null;
    }

    private DocumentTemplate mapToDomain(DocumentTemplateJpaEntity documentTemplateJpaEntity) {
        DocumentTemplate documentTemplate = ObjectMapperUtils.map(documentTemplateJpaEntity, DocumentTemplate.class);
        return documentTemplate;
    }
}
