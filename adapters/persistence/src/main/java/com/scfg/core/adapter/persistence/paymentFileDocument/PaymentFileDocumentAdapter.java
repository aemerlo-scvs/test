package com.scfg.core.adapter.persistence.paymentFileDocument;


import com.scfg.core.application.port.out.PaymentFileDocumentPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.PaymentFileDocument;
import lombok.RequiredArgsConstructor;


@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentFileDocumentAdapter implements PaymentFileDocumentPort {
    private final PaymentFileDocumentRepository paymentFileDocumentRepository;

    @Override
    public Long saveOrUpdate(PaymentFileDocument paymentFileDocument) {
        PaymentFileDocumentJpaEntity paymentFileDocumentJpaEntity = mapToEntity(paymentFileDocument);
        paymentFileDocumentJpaEntity = this.paymentFileDocumentRepository.save(paymentFileDocumentJpaEntity);
        return paymentFileDocumentJpaEntity.getId();
    }

    @Override
    public Long getMaxNumberTransactionFileDocument() {
        Long maxNumber = this.paymentFileDocumentRepository.getMaxNumberPaymentFileDocument();
        return maxNumber == null ? 1L : maxNumber + 1;
    }

    private PaymentFileDocumentJpaEntity mapToEntity(PaymentFileDocument document) {
        PaymentFileDocumentJpaEntity paymentFileDocumentJpaEntity = PaymentFileDocumentJpaEntity.builder()
                .id(document.getId())
                .description(document.getDescription())
                .documentNumber(document.getDocumentNumber())
                .content(document.getContent())
                .documentTypeIdc(document.getDocumentTypeIdc())
                .mimeType(document.getMimeType())
                .createdAt(document.getCreatedAt())
                .lastModifiedAt(document.getLastModifiedAt())
                .build();
        return paymentFileDocumentJpaEntity;
    }

    private PaymentFileDocument mapToDomain(PaymentFileDocumentJpaEntity o) {
        PaymentFileDocument paymentFileDocument = PaymentFileDocument.builder()
                .id(o.getId())
                .createdAt(o.getCreatedAt())
                .lastModifiedAt(o.getLastModifiedAt())
                .build();
        return paymentFileDocument;
    }
}
