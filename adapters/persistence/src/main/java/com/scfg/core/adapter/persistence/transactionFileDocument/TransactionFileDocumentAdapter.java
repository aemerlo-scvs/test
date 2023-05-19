package com.scfg.core.adapter.persistence.transactionFileDocument;


import com.scfg.core.application.port.out.TransactionFileDocumentPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.TransactionFileDocument;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@PersistenceAdapter
@RequiredArgsConstructor
public class TransactionFileDocumentAdapter implements TransactionFileDocumentPort {
    private final TransactionFileDocumentRepository transactionFileDocumentRepository;

    @Override
    public Long saveOrUpdate(TransactionFileDocument transactionFileDocument) {
        TransactionFileDocumentJpaEntity transactionFileDocumentJpaEntity = mapToEntity(transactionFileDocument);
        transactionFileDocumentJpaEntity = this.transactionFileDocumentRepository.save(transactionFileDocumentJpaEntity);
        return  transactionFileDocumentJpaEntity.getId();
    }

    @Override
    public TransactionFileDocument findByTransactionFileDocumentId(Long transactionFileDocumentId) {
        TransactionFileDocumentJpaEntity transactionFileDocumentJpaEntity = this.transactionFileDocumentRepository.findById(
                transactionFileDocumentId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        if(transactionFileDocumentJpaEntity == null) {return null;}
        return mapToDomain(transactionFileDocumentJpaEntity);
    }

    @Override
    public Long getMaxNumberTransactionFileDocument() {
        Long maxNumber = this.transactionFileDocumentRepository.getMaxNumberTransactionFileDocument();
        return maxNumber == null ? 1L : maxNumber + 1L;
    }


    private TransactionFileDocumentJpaEntity mapToEntity(TransactionFileDocument document) {
       return TransactionFileDocumentJpaEntity.builder()
                .id(document.getId())
                .description(document.getDescription())
                .documentNumber(document.getDocumentNumber())
                .content(document.getContent())
                .documentTypeIdc(document.getDocumentTypeIdc())
                .mimeType(document.getMimeType())
                .createdAt(document.getCreatedAt())
                .lastModifiedAt(document.getLastModifiedAt())
                .build();
    }

    private TransactionFileDocument mapToDomain(TransactionFileDocumentJpaEntity o) {
        return TransactionFileDocument.builder()
                .id(o.getId())
                .createdAt(o.getCreatedAt())
                .lastModifiedAt(o.getLastModifiedAt())
                .description(o.getDescription())
                .documentTypeIdc(o.getDocumentTypeIdc())
                .documentNumber(o.getDocumentNumber())
                .content(o.getContent())
                .mimeType(o.getMimeType())
                .build();
    }
}
