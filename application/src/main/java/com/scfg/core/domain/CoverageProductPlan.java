package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class CoverageProductPlan extends BaseDomain {

    private Double insuredCapitalCoverage;

    // Cambiar esto en una relación influye en la consulta de ProductRepository

    private Long coverageProductId;

    // Cambiar esto en una relación influye en la consulta de ProductRepository

    private Long planId;

    private Integer minimumEntryAge;

    private Integer entryAgeLimit;

    private Integer ageLimitStay;

    private Double rate;
}
