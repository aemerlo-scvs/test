package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Plan extends BaseDomain implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Double totalPremium;

    private Integer rate;

    private Double totalInsuredCapital;

    private Integer applyDiscount;

    private Double creditPremiumSurcharge;

    private Integer currencyTypeIdc;

    private Long productId;

    private Integer agreementCode;

}
