package com.scfg.core.adapter.persistence.annexeFileDocument;

import com.scfg.core.application.port.out.AnnexeFileDocumentPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.exception.NotDataFoundException;
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

    @Override
    public Document getFileOptionalOrException(Long documentId) {
        AnnexeFileDocumentJpaEntity documet = annexeFileDocumentRepository.findById(documentId)
                .orElseThrow(()-> new NotDataFoundException("documentId: " + documentId + " Not found"));
        return new ModelMapper().map(documet, Document.class);
    }

    @Override
    public Document getFileByRequestAnnexeIdAndTypeDocumentIdc(Long requestAnnexeId, Integer typeDocumentIdc) {
        AnnexeFileDocumentJpaEntity document = annexeFileDocumentRepository.findRequestAnnexeIdAndAnnexeTypeIdc(requestAnnexeId, typeDocumentIdc);
        return new ModelMapper().map(document, Document.class);
    }

    @Override
    public Document findRequestAnnexeIdAndAnnexeTypeIdcAndSigned(Long requestAnnexeId, Integer typeDocumentIdc, Boolean signed) {
        AnnexeFileDocumentJpaEntity document = annexeFileDocumentRepository.findRequestAnnexeIdAndAnnexeTypeIdcAndSigned(requestAnnexeId, typeDocumentIdc, signed);
        return new ModelMapper().map(document, Document.class);
    }

    @Override
    public Long getNextNumber(Integer documentTypeIdc) {
        Long maxNumber = this.annexeFileDocumentRepository.getMaxNumber(documentTypeIdc);
        return maxNumber == null ? 1L : maxNumber + 1;
    }

    private AnnexeFileDocumentJpaEntity mapToJpaEntity(Document annexeFileDocument) {
        return new ModelMapper().map(annexeFileDocument, AnnexeFileDocumentJpaEntity.class);
    }



}
