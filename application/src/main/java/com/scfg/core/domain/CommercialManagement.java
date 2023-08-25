package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class CommercialManagement extends BaseDomain {
    private Long id;
    private String commercialManagementId;
    private Long policyId;
    private Date endOfCoverage;
    private Integer managementStatusIdc;
    private Integer managementSubStatusIdc;
    private Long assignedUserId;
    private Date messageSentDate;
    private Integer linkEntries;
    private Date firstEntryDate;
    private Date lastEntryDate;
    private Integer renewalPlanId;

}
