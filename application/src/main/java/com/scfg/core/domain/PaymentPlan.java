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
public class PaymentPlan extends BaseDomain {

    private Integer quoteNumber;

    private Double amount;

    private Double amountPaid;

    private Double residue;

    private Integer percentage;

    private LocalDateTime expirationDate;

    private LocalDateTime rescheduleDate;

    private LocalDateTime datePaid;

    private Long paymentId;

}
