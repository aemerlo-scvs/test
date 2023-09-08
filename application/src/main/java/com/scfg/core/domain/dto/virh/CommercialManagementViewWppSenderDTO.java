package com.scfg.core.domain.dto.virh;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class CommercialManagementViewWppSenderDTO {

    private Long commercialManagementId;
    private Long generalRequestId;

    private Integer dateDifference;

    private Date endOfCoverage;
    private Date startOfCoverage;

    private Integer prioritySender;

    private Integer managementStatusIdc;

    private String managementStatus;

    private Integer managementSubStatusIdc;

    private String managementSubStatus;

    private String productName;

    private String insured;

    private String number;

    private String numberPolicy;

    private String uniqueCode;
}
