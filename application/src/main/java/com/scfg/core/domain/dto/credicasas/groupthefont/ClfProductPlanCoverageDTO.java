package com.scfg.core.domain.dto.credicasas.groupthefont;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class ClfProductPlanCoverageDTO {

    private Long coverageId;
    private Integer coverageType;
    private String coverageName;
    private Long coveragePolicyItemId;
    private Long coverageProductPlanId;
    private Long coverageProductId;
    private Long parentCoverageProductId;
    private Long policyItemId;
    private Double insuredCapital;
    private Double insuredCapitalTemp;
    private Double additionalPremiumPerPercentage;
    private Double additionalPremiumPerThousand;
    private String comment;
    private Integer status;
    private Boolean isChecked;
    private Date createdAt;
    private Date lastModifiedAt;
    private Integer order;
    private Double rate;

}
