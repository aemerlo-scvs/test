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
public class PolicyItemMathReserve extends BaseDomain {
    private Integer insuranceValidity;

    private Integer year;

    private Float value;

    private Long policyItemId;

    private Integer status;
}
