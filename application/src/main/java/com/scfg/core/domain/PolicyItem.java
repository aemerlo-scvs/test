package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import com.scfg.core.domain.dto.credicasas.groupthefont.requestDto.RequestFontDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class PolicyItem extends BaseDomain {
    private Long personId;
    private Long policyId;
    private Long generalRequestId;
    private Double individualInsuredCapital;
    private Date validityStart;
    private Date termValidity;
    private Double individualPremium;
    private Double individualPremiumRate;
    private Integer riskPositionIdc;
    private LocalDateTime pronouncementDate;

    private Double individualAdditionalPremium;

    private Double individualNetPremium;

    private Double individualRiskPremium;

    private Double APS;

    private Double FPA;

    private Double IVA;

    private Double IT;

    private Double individualIntermediaryCommission;

    private Double individualCollectionServiceCommission;

    private Long annexeId;

    private Integer itemStatusIdc;

    private LocalDateTime inclusionDate;

    private LocalDateTime exclusionDate;


    //#region Custom Constructors

    // SMVS Constructor
    public PolicyItem(Policy policy, Long personId, Long generalRequestId) {
        this.personId = personId;
        this.policyId = policy.getId();
        this.generalRequestId = generalRequestId;
        this.individualPremium = policy.getTotalPremium();
        this.individualAdditionalPremium = 0.0;
        this.individualInsuredCapital = policy.getInsuredCapital();
        this.validityStart = policy.getFromDate();
        this.termValidity = policy.getToDate();
        this.riskPositionIdc = 1;
    }

    //#enregion

    // GEL Constructor
    public PolicyItem(RequestFontDTO requestFontDTO, Long personId, Long requestId, double individualPremiumRate) {
        this.personId = personId;
        this.policyId = requestFontDTO.getCompany().getPolicyDto().get(0).getId();
        this.generalRequestId = requestId;
        this.individualPremium = 0.0;
        this.individualInsuredCapital = requestFontDTO.getCredit().getRequestedAmount();
        this.individualPremiumRate = individualPremiumRate;
        this.validityStart = Date.from(Instant.now());
        this.termValidity = requestFontDTO.getCompany().getPolicyDto().get(0).getToDate();
        this.riskPositionIdc = null;
        this.pronouncementDate = LocalDateTime.now();
    }

    // VIN Constructor
    public PolicyItem(Policy policy, Long personId, Long generalRequestId, Double individualPremium) {
        this.personId = personId;
        this.policyId = policy.getId();
        this.generalRequestId = generalRequestId;
        this.individualPremium = individualPremium;
        this.individualInsuredCapital = policy.getInsuredCapital();
        this.validityStart = policy.getFromDate();
        this.termValidity = policy.getToDate();
        this.riskPositionIdc = 1;
    }
    //#enregion

}
