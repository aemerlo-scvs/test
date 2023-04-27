package com.scfg.core.adapter.persistence.paymentPlan;

import com.scfg.core.application.port.out.PaymentPlanPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.PaymentPlan;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentPlanPersistenceAdapter implements PaymentPlanPort {

    private final PaymentPlanRepository paymentPlanRepository;

    @Override
    public long saveOrUpdate(PaymentPlan paymentPlan) {
        PaymentPlanJpaEntity paymentPlanJpaEntity = mapToJpaEntity(paymentPlan);
        paymentPlanJpaEntity = paymentPlanRepository.save(paymentPlanJpaEntity);
        return paymentPlanJpaEntity.getId();
    }

    //#region Mappers

    public static PaymentPlanJpaEntity mapToJpaEntity(PaymentPlan paymentPlan) {
        PaymentPlanJpaEntity paymentPlanJpaEntity = PaymentPlanJpaEntity.builder()
                .id(paymentPlan.getId())
                .quoteNumber(paymentPlan.getQuoteNumber())
                .amount(paymentPlan.getAmount())
                .amountPaid(paymentPlan.getAmountPaid())
                .residue(paymentPlan.getResidue())
                .percentage(paymentPlan.getPercentage())
                .expirationDate(paymentPlan.getExpirationDate())
                .rescheduleDate(paymentPlan.getRescheduleDate())
                .datePaid(paymentPlan.getDatePaid())
                .paymentId(paymentPlan.getPaymentId())
                .createdAt(paymentPlan.getCreatedAt())
                .lastModifiedAt(paymentPlan.getLastModifiedAt())
                .build();
        return  paymentPlanJpaEntity;
    }

    //#endregion

}
