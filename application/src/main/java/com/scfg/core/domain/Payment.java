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
public class Payment extends BaseDomain {

    private Integer paymentTypeIdc;

    private Integer paymentPeriod;

    private Double total;

    private Double surcharge;

    private Double totalSurcharge;

    private Double totalPaid;

    private Integer currencyTypeIdc;

    private Integer quoteQuantity;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long generalRequestId;
}
