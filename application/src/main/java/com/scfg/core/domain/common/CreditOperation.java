package com.scfg.core.domain.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CreditOperation extends BaseDomain {

    private Long operationNumber;

    private Double disbursedAmount; // Bs

    private Integer creditLine;

    private Double insuredAmount;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate disbursementDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate expirationDate;

    private Integer deadlineDays;

    private Double extraPremiumRate;

    private Double extraPremiumValue;

    private Double premiumRate;

    private Double premiumValue;

    // Relationships (identifieres)
    private Long currencyId;
    private Long insuranceRequestId;
    //private Long requestId;

    // Relationships (object)
    private Classifier currency;
    private InsuranceRequest insuranceRequest;
}
