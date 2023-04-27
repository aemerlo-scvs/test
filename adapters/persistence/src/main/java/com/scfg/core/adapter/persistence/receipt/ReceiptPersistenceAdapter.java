package com.scfg.core.adapter.persistence.receipt;

import com.scfg.core.application.port.out.ReceiptPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.GeneralRequest;
import com.scfg.core.domain.Receipt;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class ReceiptPersistenceAdapter implements ReceiptPort {

    private final ReceiptRepository receiptRepository;

    @Override
    public long saveOrUpdate(Receipt receipt) {
        ReceiptJpaEntity receiptJpaEntity = mapToJpaEntity(receipt);
        receiptJpaEntity = receiptRepository.save(receiptJpaEntity);
        return receiptJpaEntity.getId();
    }

    //#region Mappers

    public static ReceiptJpaEntity mapToJpaEntity(Receipt receipt) {
        ReceiptJpaEntity receiptJpaEntity = ReceiptJpaEntity.builder()
                .id(receipt.getId())
                .voucherNumber(receipt.getVoucherNumber())
                .paymentDate(receipt.getPaymentDate())
                .concept(receipt.getConcept())
                .totalAmount(receipt.getTotalAmount())
                .literalAmount(receipt.getLiteralAmount())
                .sellerId(receipt.getSellerId())
                .sellerName(receipt.getSellerName())
                .salePlace(receipt.getSalePlace())
                .agencyId(receipt.getAgencyId())
                .agencyName(receipt.getAgencyName())
                .observation(receipt.getObservation())
                .receiptStatusIdc(receipt.getReceiptStatusIdc())
                .transactionId(receipt.getTransactionId())
                .createdAt(receipt.getCreatedAt())
                .lastModifiedAt(receipt.getLastModifiedAt())
                .build();

        return receiptJpaEntity;
    }

    @Override
    public Receipt gReceiptForNumberReceipt(String voucherNumber) {
        // TODO Auto-generated method stub
        Optional<ReceiptJpaEntity> jpaEntity = receiptRepository.findByReceiptJpaEntityVoucher(voucherNumber);

        return jpaEntity.isPresent() ? mapToDomain(jpaEntity.get()) : null;
    }

    @Override
    public Receipt getReceiptForGeneralRequestById(Long requestId) {
        List<ReceiptJpaEntity> list = receiptRepository.findReceiptByGeneralRequestId(requestId);
        return (list.size() > 0) ? mapToDomain(list.get(0)) : null;
    }

    @Override
    public Receipt getReceiptForGeneralRequestByIdList(List<Long> request) {
        List<ReceiptJpaEntity> list = receiptRepository.findReceiptByGeneralRequestIdList(request);
        return (list.size() > 0) ? mapToDomain(list.get(0)) : null;
    }

    private Receipt mapToDomain(ReceiptJpaEntity receiptJpaEntity) {
        Receipt receipt = Receipt.builder()
                .id(receiptJpaEntity.getId())
                .voucherNumber(receiptJpaEntity.getVoucherNumber())
                .paymentDate(receiptJpaEntity.getPaymentDate())
                .concept(receiptJpaEntity.getConcept())
                .totalAmount(receiptJpaEntity.getTotalAmount())
                .sellerId(receiptJpaEntity.getSellerId())
                .sellerName(receiptJpaEntity.getSellerName())
                .salePlace(receiptJpaEntity.getSalePlace())
                .agencyId(receiptJpaEntity.getAgencyId())
                .agencyName(receiptJpaEntity.getAgencyName())
                .observation(receiptJpaEntity.getObservation())
                .receiptStatusIdc(receiptJpaEntity.getReceiptStatusIdc())
                .transactionId(receiptJpaEntity.getTransactionId())
                .createdAt(receiptJpaEntity.getCreatedAt())
                .lastModifiedAt(receiptJpaEntity.getLastModifiedAt())
                .build();
        return receipt;
    }
}
