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

    private String name;

    private String description;

    private Double totalPremium;

    private Integer rate;

    private Integer applyDiscount;

    private Integer currencyTypeIdc;
    private Integer status;

    private Long productId;
    private  Integer bfsAgreementCode;

}
