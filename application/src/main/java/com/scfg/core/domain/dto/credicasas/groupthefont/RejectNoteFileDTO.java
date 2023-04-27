package com.scfg.core.domain.dto.credicasas.groupthefont;

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
public class RejectNoteFileDTO {
    private Integer citeNumber;
    private String groupName;
    private Long requestNumber;
    private String policyName;
    private String fullNameInsured;
    private String documentNumber;
    private Double currentCreditAmount;
    private Double newRequestCreditAmount;
    private Double totalCumulusAmount;
    private Integer creditPeriodRequested;
    private Integer currencyType;
    private List<String> rejectionReason;
    private Boolean pathologyMedic;
    private String rejectComment;
    private String userRegional;
    private String userOffice;
}
