package com.scfg.core.domain.common;

import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "Model para registrar los datos economicos del item de la poliza por cobertura")
public class PolicyItemEconomicReinsurance extends BaseDomain {
    private Long policyItemEconomicId;
    private Double premiumCeded;
    private Double premiumRetained;
    private Long coverageId;
    private Long reinsurerId;

    private Integer status;


    //#region Constructors

    public PolicyItemEconomicReinsurance(Long policyItemEconomicId, Long coverageId) {
        this.policyItemEconomicId = policyItemEconomicId;
        this.coverageId = coverageId;
        this.reinsurerId = 1L;
    }

    //#endregion
}
