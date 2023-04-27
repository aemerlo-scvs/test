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
public class ReportBeneficiaries {
    private String partyFullName;
    private String partyRelationType;
    private double percentage;
}
