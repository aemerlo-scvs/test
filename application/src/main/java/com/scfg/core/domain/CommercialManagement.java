package com.scfg.core.domain;

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
public class CommercialManagement {
    private Long id;
    private UUID commercialManagementId;
    private Long policyId;
    private Date endOfCoverage;
    private Integer managementStatusIdc;
    private Integer managementSubStatusIdc;
    private Long assignedUserId;
    private Date messageSentDate;
    private Integer linkEntries;
    private Date firstEntryDate;
    private Date lastEntryDate;

}
