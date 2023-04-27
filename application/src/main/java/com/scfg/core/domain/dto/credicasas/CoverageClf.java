package com.scfg.core.domain.dto.credicasas;

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
public class CoverageClf {
    private String name;

    private Integer code;

    private Integer coverageTypeIdc;

    private Long coverageProductPlanId;

    private Double insuredCapitalCoverage;

    private Long coverageProductId;

    private Long parentCoverageProductId;

    private Long planId;

    private Integer minimumEntryAge;

    private Integer entryAgeLimit;

    private Integer ageLimitStay;

    private Double rate;

    private Integer order;
}
