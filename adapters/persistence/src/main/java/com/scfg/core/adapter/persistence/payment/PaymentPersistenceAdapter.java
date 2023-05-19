package com.scfg.core.adapter.persistence.payment;

import com.scfg.core.application.port.out.PaymentPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PaymentTypeEnum;
import com.scfg.core.common.enums.PeriodicityEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.Payment;
import config.ModelMapperConfig;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements PaymentPort {

    private final PaymentRepository paymentRepository;

    @Override
    public long saveOrUpdate(Payment payment) {
        PaymentJpaEntity paymentJpaEntity = mapToJpaEntity(payment);
        paymentJpaEntity = paymentRepository.save(paymentJpaEntity);
        return paymentJpaEntity.getId();
    }

    @Override
    public Payment findByGeneralRequest(Long generalRequestId) {
        List<PaymentJpaEntity> list = this.paymentRepository.findAllByGeneralRequestId(generalRequestId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        if (list.isEmpty()) {
            throw new NotDataFoundException("Pago: de la solicitud : " + generalRequestId.toString() + " No encontrado");
        }
        PaymentJpaEntity paymentJpaEntity = list.get(0);
        return new ModelMapperConfig().getStrictModelMapper().map(paymentJpaEntity, Payment.class);
    }

    //#region Mappers

    public static PaymentJpaEntity mapToJpaEntity(Payment payment) {
        PaymentJpaEntity paymentJpaEntity = PaymentJpaEntity.builder()
                .id(payment.getId())
                .paymentTypeIdc(payment.getPaymentTypeIdc())
                .paymentPeriod(payment.getPaymentPeriod())
                .total(payment.getTotal())
                .surcharge(payment.getSurcharge())
                .totalSurcharge(payment.getTotalSurcharge())
                .totalPaid(payment.getTotalPaid())
                .currencyTypeIdc(payment.getCurrencyTypeIdc())
                .quoteQuantity(payment.getQuoteQuantity())
                .startDate(payment.getStartDate())
                .endDate(payment.getEndDate())
                .generalRequestId(payment.getGeneralRequestId())
                .createdAt(payment.getCreatedAt())
                .lastModifiedAt(payment.getLastModifiedAt())
                .annexeId(payment.getAnnexeId())
                .paymentFileDocumentId(payment.getPaymentFileDocumentId())
                .build();

        return  paymentJpaEntity;
    }

    //#endregion
}
