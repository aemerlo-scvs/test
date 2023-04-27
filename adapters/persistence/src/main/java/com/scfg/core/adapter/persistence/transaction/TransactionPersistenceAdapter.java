package com.scfg.core.adapter.persistence.transaction;

import com.scfg.core.application.port.out.TransactionPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@PersistenceAdapter
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements TransactionPort {

    private final TransactionRepository transactionRepository;

    @Override
    public long saveOrUpdate(Transaction transaction) {
        TransactionJpaEntity transactionJpaEntity = mapToJpaEntity(transaction);
        transactionJpaEntity = transactionRepository.save(transactionJpaEntity);
        return transactionJpaEntity.getId();
    }

    @Override
    public Transaction findById(long id) {
        Optional<TransactionJpaEntity> transaction = transactionRepository.findById(id);
        return transaction.get() != null ? mapToDomain(transaction.get()) : null;
    }

    private Transaction mapToDomain(TransactionJpaEntity transactionJpaEntity) {
        Transaction transaction=Transaction.builder()
                .id(transactionJpaEntity.getId())
                .paymentPlanId(transactionJpaEntity.getPaymentPlanId())
                .amount(transactionJpaEntity.getAmount())
                .remainAmount(transactionJpaEntity.getRemainAmount())
                .datePaid(transactionJpaEntity.getDatePaid())
                .currencyTypeIdc(transactionJpaEntity.getCurrencyTypeIdc())
                .exchangeRate(transactionJpaEntity.getExchangeRate())
                .annulmentDate(transactionJpaEntity.getAnnulmentDate())
                .annulmentReason(transactionJpaEntity.getAnnulmentReason())
                .paymentChannelIdc(transactionJpaEntity.getPaymentChannelIdc())
                .transactionType(transactionJpaEntity.getTransactionType())
                .observation(transactionJpaEntity.getObservation())
                .voucherNumber(transactionJpaEntity.getVoucherNumber())
                .createdAt(transactionJpaEntity.getCreatedAt())
                .lastModifiedAt(transactionJpaEntity.getLastModifiedAt())
                .build();
        return transaction;
    }

    //#region Mappers

    public static TransactionJpaEntity mapToJpaEntity(Transaction transaction) {
        TransactionJpaEntity transactionJpaEntity = TransactionJpaEntity.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .remainAmount(transaction.getRemainAmount())
                .datePaid(transaction.getDatePaid())
                .currencyTypeIdc(transaction.getCurrencyTypeIdc())
                .exchangeRate(transaction.getExchangeRate())
                .annulmentDate(transaction.getAnnulmentDate())
                .annulmentReason(transaction.getAnnulmentReason())
                .paymentChannelIdc(transaction.getPaymentChannelIdc())
                .transactionType(transaction.getTransactionType())
                .observation(transaction.getObservation())
                .paymentPlanId(transaction.getPaymentPlanId())
                .voucherNumber(transaction.getVoucherNumber())
                .createdAt(transaction.getCreatedAt())
                .lastModifiedAt(transaction.getLastModifiedAt())
                .build();
        return transactionJpaEntity;
    }

    //#endregion

}
