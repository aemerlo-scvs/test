package com.scfg.core.domain.common;

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
public class Configuration extends BaseDomain{
    private String numberFormat;

    private Integer numberDigits;

    private String dateFormat;

    private String timeFormat;
}
