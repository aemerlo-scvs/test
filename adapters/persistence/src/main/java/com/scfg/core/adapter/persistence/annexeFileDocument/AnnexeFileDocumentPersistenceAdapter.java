package com.scfg.core.adapter.persistence.annexeFileDocument;

import com.scfg.core.application.port.out.AnnexeFileDocumentPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.Document;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class AnnexeFileDocumentPersistenceAdapter implements AnnexeFileDocumentPort {
    private final AnnexeFileDocumentRepository annexeFileDocumentRepository;


    @Override
    public List<Document> findAll() {
        Object o = annexeFileDocumentRepository.findAll();
        return (List<Document>) o;
    }

    @Override
    public Long saveOrUpdate(Document o) {
        AnnexeFileDocumentJpaEntity annexeFileDocumentJpaEntity = mapToJpaEntity(o);
        annexeFileDocumentJpaEntity = annexeFileDocumentRepository.save(annexeFileDocumentJpaEntity);
        return annexeFileDocumentJpaEntity.getId();
    }

    private AnnexeFileDocumentJpaEntity mapToJpaEntity(Document annexeFileDocument) {
        return new ModelMapper().map(annexeFileDocument, AnnexeFileDocumentJpaEntity.class);
    }



}
