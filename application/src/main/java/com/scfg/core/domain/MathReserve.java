package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MathReserve extends BaseDomain {
    private Integer vigencyYears;
    private Integer age;
    private Float value;
    private Integer status;
    private Float insuredCapital;
    private Float percentageRate;
}