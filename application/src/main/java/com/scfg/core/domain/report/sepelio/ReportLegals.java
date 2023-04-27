package com.scfg.core.domain.report.sepelio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ReportLegals {
    private String fullNames;
    private String fullNameLegal;
    private String legalExt;
    private String ciLegal;
    private String relation;
    private String fullNameBeneficiary;
    private Double percentage;
}
