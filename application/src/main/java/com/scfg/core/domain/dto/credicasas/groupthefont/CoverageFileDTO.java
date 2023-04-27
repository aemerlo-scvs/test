package com.scfg.core.domain.dto.credicasas.groupthefont;

import com.scfg.core.domain.Beneficiary;
import com.scfg.core.domain.dto.FileDocumentDTO;
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
public class CoverageFileDTO {
    private String policyNumber;
    private Integer certificateNumber;
    private String policyHolderName;
    private String policyHolderTelephone;
    private String policyHolderAddress;
    private String insuredName;
    private List<Beneficiary> beneficiaryList;
    private List<CoveragePair> coverageList;
    private String insuredStartDateStr;
    private String insuredFinishDateStr;
    private Double premiumMonthly;
    private Double extraPremiumMonthly;
    private Double totalRate;
    private Double totalPremiumMonthly;
    private String extraPremiumAnswer;
    private List<CoveragePair> coverageLimitAgeList;
    private Long userRegionalIdc;
    private FileDocumentDTO digitalFirm;
    private String brokerName;
    private String brokerDirectionAndNumber;
    private Long djsNumber;
    private Integer legalHeirs;
    private String exclusionComment;
    private Long insuredTypeIdc;
}
