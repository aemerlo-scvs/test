package com.scfg.core.domain.dto;

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
public class CoverageDTO {
    private Long coverageId;

    private Integer coverageTypeIdc;

    private String coverageName;

    private Long coverageProductId;

    private Long parentCoverageProductId;

    private Long coverageProductPlanId;

    private Double insuredCapitalCoverage;

    private Double rate;

    private Integer ageLimitStay;

    private Integer entryAgeLimit;

    private Integer minimumEntryAge;

    private Integer order;
}
