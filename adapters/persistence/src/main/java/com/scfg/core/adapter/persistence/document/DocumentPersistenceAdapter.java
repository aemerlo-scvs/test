package com.scfg.core.adapter.persistence.document;

import com.scfg.core.adapter.persistence.person.PersonJpaEntity;
import com.scfg.core.application.port.out.DocumentPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.Document;
import com.scfg.core.domain.dto.FileDocumentDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class DocumentPersistenceAdapter implements DocumentPort {
    private final DocumentRepository documentRepository;

    @Override
    public List<Document> getAllDocument() {
        Object documentRepositoryAll = documentRepository.findAll();
        return (List<Document>) documentRepositoryAll;
    }

    @Override
    public Boolean saveOrUpdate(Document document) {
        DocumentJpaEntity documentJpaEntity = mapToJpaEntity(document);
        try {
            documentRepository.save(documentJpaEntity);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Boolean saveOrUpdate(FileDocumentDTO file, long personId) {
        DocumentJpaEntity documentJpaEntity = mapToJpaEntityCLF(file,personId);
        try {
            documentRepository.save(documentJpaEntity);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    private DocumentJpaEntity mapToJpaEntityCLF(FileDocumentDTO file, long personId) {
        return DocumentJpaEntity.builder()
                .documentTypeIdc(file.getTypeId())
                .id(file.getId())
                .personJpaEntity(PersonJpaEntity.builder().id(personId).build())
                .content(file.getContent())
                .mimeType(file.getMime())
                .descripcion(file.getName())
                .build();
    }

    private DocumentJpaEntity mapToJpaEntity(Document document) {
        return DocumentJpaEntity.builder()
                .id(document.getId())
                .descripcion(document.getDescription())
                .content(document.getContent())
                .documentTypeIdc(document.getDocumentTypeIdc())
                .documentUrl(document.getDocumentUrl())
                .mimeType(document.getMimeType())
                .personJpaEntity(PersonJpaEntity.builder().id(document.getPersonId()).build())
                .createdAt(document.getCreatedAt())
                .lastModifiedAt(document.getLastModifiedAt())
                .build();
    }
}
