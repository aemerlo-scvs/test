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
public class Receipt extends BaseDomain {

    private String voucherNumber;

    private LocalDateTime paymentDate;

    private String concept;

    private Double totalAmount;

    private String literalAmount;

    private Integer sellerId;

    private String sellerName;

    private String salePlace;

    private Integer agencyId;

    private String agencyName;

    private String observation;

    private Integer receiptStatusIdc;

    private Long transactionId;
}
