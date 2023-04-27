package com.scfg.core.adapter.persistence.documentTemplate;

import com.scfg.core.application.port.out.DocumentTemplatePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.common.DocumentTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private DocumentTemplate mapToDomain(DocumentTemplateJpaEntity documentTemplateJpaEntity) {
        DocumentTemplate documentTemplate = DocumentTemplate.builder()
                .id(documentTemplateJpaEntity.getId())
                .createdAt(documentTemplateJpaEntity.getCreatedAt())
                .lastModifiedAt(documentTemplateJpaEntity.getLastModifiedAt())
                .documentUrl(documentTemplateJpaEntity.getDocumentUrl())
                .idDocumentTemplate(documentTemplateJpaEntity.getIdDocumentTemplate())
                .description(documentTemplateJpaEntity.getDescription())
                .documentTypeIdc(documentTemplateJpaEntity.getDocumentTypeIdc())
                .build();
        return documentTemplate;
    }
}
