package com.scfg.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class CommercialManagementDTO {
    private String codeUUID;
    private Long policyId;
    private String policyNumber;
    private String productName;
    private String personFullName;
    private Date policyDueDate;
    private Integer daysPassed;
    private String policyState;
    private String renewalState;
    private String managerUsername;
}
