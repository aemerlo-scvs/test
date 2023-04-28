package com.scfg.core.domain.common;

import com.scfg.core.common.enums.CommissionIndicatorClassifierEnum;
import com.scfg.core.common.enums.IntermediaryTypeClassifierEnum;
import com.scfg.core.common.enums.PolicySubMovementTypeClassifierEnum;
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
@ApiModel(description = "Model para registrar los datos economicos del item de la poliza")
public class PolicyItemEconomic extends BaseDomain {

    private Long policyItemId;
    private Long annexeId;
    private Integer movementTypeIdc;
    private Integer movementSubTypeIdc;
    private Double exchangeRate;
    private Double individualPremium;
    private Double individualNetPremium;
    private Double individualAdditionalPremium;
    private Double individualRiskPremium;
    private Double APS;
    private Double FPA;
    private Double IVA;
    private Double IT;
    private Integer intermediaryTypeIdc;
    private Long intermediaryId;
    private Integer individualIntermediaryCommissionIndicatorIdc;
    private Double individualIntermediaryCommissionPercentage;
    private Double individualIntermediaryCommission;
    private Integer individualCollectionServiceCommissionIndicatorIdc;
    private Double individualCollectionServiceCommission;
    private Double individualPremiumCeded;
    private Integer status;

    //#region Constructors

    public PolicyItemEconomic(Long policyItemId, Integer movementTypeIdc, Long intermediaryId, Double individualPremium) {
        this.policyItemId = policyItemId;
        this.movementTypeIdc = movementTypeIdc;
        this.movementSubTypeIdc = PolicySubMovementTypeClassifierEnum.NEW.getValue();
        this.intermediaryTypeIdc = IntermediaryTypeClassifierEnum.BROKER.getValue();
        this.individualIntermediaryCommissionIndicatorIdc = CommissionIndicatorClassifierEnum.NET_PREMIUM.getValue();
        this.individualCollectionServiceCommissionIndicatorIdc = CommissionIndicatorClassifierEnum.TOTAL_PREMIUM.getValue();
        this.intermediaryId = intermediaryId;
        this.individualPremium = individualPremium;
    }

    public PolicyItemEconomic(PolicyItemEconomic newPolicyItemEconomic) {
        this.policyItemId = newPolicyItemEconomic.policyItemId;
        this.annexeId = newPolicyItemEconomic.annexeId;
        this.movementTypeIdc = newPolicyItemEconomic.movementTypeIdc;
        this.movementSubTypeIdc = newPolicyItemEconomic.movementSubTypeIdc;
        this.exchangeRate = newPolicyItemEconomic.exchangeRate;
        this.individualPremium = newPolicyItemEconomic.individualPremium;
        this.individualNetPremium = newPolicyItemEconomic.individualNetPremium;
        this.individualAdditionalPremium = newPolicyItemEconomic.individualAdditionalPremium;
        this.individualRiskPremium = newPolicyItemEconomic.individualRiskPremium;
        this.APS = newPolicyItemEconomic.APS;
        this.FPA = newPolicyItemEconomic.FPA;
        this.IVA = newPolicyItemEconomic.IVA;
        this.IT = newPolicyItemEconomic.IT;
        this.intermediaryTypeIdc = newPolicyItemEconomic.intermediaryTypeIdc;
        this.intermediaryId = newPolicyItemEconomic.intermediaryId;
        this.individualIntermediaryCommissionIndicatorIdc = newPolicyItemEconomic.individualIntermediaryCommissionIndicatorIdc;
        this.individualIntermediaryCommissionPercentage = newPolicyItemEconomic.individualIntermediaryCommissionPercentage;
        this.individualIntermediaryCommission = newPolicyItemEconomic.individualIntermediaryCommission;
        this.individualCollectionServiceCommissionIndicatorIdc = newPolicyItemEconomic.individualCollectionServiceCommissionIndicatorIdc;
        this.individualCollectionServiceCommission = newPolicyItemEconomic.individualCollectionServiceCommission;
        this.individualPremiumCeded = newPolicyItemEconomic.individualPremiumCeded;

        this.setCreatedBy(newPolicyItemEconomic.getCreatedBy());
        this.setLastModifiedBy(newPolicyItemEconomic.getLastModifiedBy());
    }

    //#endregion

}
