package com.scfg.core.domain.dto;

import com.scfg.core.domain.CoveragePlan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class CommercialManagementInfoPolicyDTO {
    private String codeUUID;
    private Long policyId;
    private String policyNumber;
    private String productName;
    private String policyPlanName;
    private String policyFromDate;
    private String policyToDate;
    private List<CoveragePlan> policyCoverages;
    private String personFullName;
    private String personCellphone;
    private String managerUsername;
}
