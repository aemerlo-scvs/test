package com.scfg.core.domain.dto;

import com.scfg.core.domain.CoveragePlan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class CommercialManagementDTO {
    private Long policyId;
    private String numberPolicy;
    private String productName;
    private String productInitials;
    private String insured;
    private String identificationNumber;
    private String policyStatus;
    private String managementStatus;
    private String managementSubStatus;
    private Integer managementStatusIdc;
    private Integer managementSubStatusIdc;
    private String userName;
    private Long userId;
    private String coverages;
    private String number; // TODO cellphone
    private String email;
    private Long planId;
    private String planName;
    private Integer dateDifference;
    private Date endOfCoverage;
    private Date issuanceDate;
    private Date fromDate;
    private String code;
    private String URL;
    private Long commercialManagementId;
}
