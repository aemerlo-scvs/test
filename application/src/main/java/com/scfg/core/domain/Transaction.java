package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Transaction extends BaseDomain {

    private Double amount;

    private Double remainAmount;

    private LocalDateTime datePaid;

    private Integer currencyTypeIdc;

    private Double exchangeRate;

    private LocalDateTime annulmentDate;

    private String annulmentReason;

    private Integer paymentChannelIdc;

    private Integer transactionType;

    private String observation;

    private Long paymentPlanId;

    private Long documentPaymentId;

    private String voucherNumber;
}
