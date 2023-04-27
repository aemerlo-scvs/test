package com.scfg.core.domain.dto.credicasas.groupthefont;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class PendingNoteFileDTO {
    private Integer citeNumber;
    private String groupName;
    private Long requestNumber;
    private String policyGroupNumber;
    private String fullNameInsured;
    private String documentNumber;
    private Double currentCreditAmount;
    private Double newRequestCreditAmount;
    private Double totalCumulusAmount;
    private Integer creditPeriodRequested;
    private Integer currencyType;
    private String userRegional;
    private String userOffice;
}
