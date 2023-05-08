package com.scfg.core.adapter.persistence.paymentPlan;

import com.scfg.core.adapter.persistence.annexe.AnnexeJpaEntity;
import com.scfg.core.application.port.out.PaymentPlanPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.PaymentPlan;
import com.scfg.core.domain.dto.vin.AnnexeDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

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

    @Override
    public PaymentPlan findByAnnexeIdOrExcepcion(Long annexeId) {
        PaymentPlanJpaEntity paymentPlanJpaEntity = paymentPlanRepository.findOptionalByAnnexeId(annexeId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue())
                .orElseThrow(() -> new NotDataFoundException("annexe: " + annexeId + " Not found"));
        return new ModelMapper().map(paymentPlanJpaEntity, PaymentPlan.class);
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
                .annexeId(paymentPlan.getAnnexeId())
                .build();
        return  paymentPlanJpaEntity;
    }

    //#endregion

}
