package com.scfg.core.domain.dto.liquidationMortgageRelief;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionReportDTO {

    private String policyType;

    private String policyCode;

    private String djsFillDate;

    private String operationNumber;

    private String requestNumber;

    private String insured;

    private String gender;

    private String documentNumber;

    private String extension;

    private String birthDate;

    private Integer age;

    private String currency;

    private Float requestAmountBs;

    private String subscriptionStatus;

    private Float extraPremiumRate;

    private String grantedCoverage;

    private String pronouncementDate;

    private String coverage;
}
