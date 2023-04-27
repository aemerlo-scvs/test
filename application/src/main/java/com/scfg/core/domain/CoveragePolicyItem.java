package com.scfg.core.domain;

import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.domain.common.BaseDomain;
import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.dto.credicasas.CoverageClf;
import com.scfg.core.domain.dto.credicasas.groupthefont.ClfProductPlanCoverageDTO;
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
public class CoveragePolicyItem extends BaseDomain {

    private Long policyItemId;
    private Long coverageProductPlanId;
    private Double insuredCapital;
    private Double additionalPremiumPerPercentage;
    private Double additionalPremiumPerThousand;
    private String comment;
    private Double rate;


    //#region Custom Constructor

    //GEL Constructors
    public CoveragePolicyItem(ClfProductPlanCoverageDTO clfProductPlanCoverageDTO) {
        this.setId(clfProductPlanCoverageDTO.getCoveragePolicyItemId());
        this.setPolicyItemId(clfProductPlanCoverageDTO.getPolicyItemId());
        this.setCoverageProductPlanId(clfProductPlanCoverageDTO.getCoverageProductPlanId());
        this.setInsuredCapital(clfProductPlanCoverageDTO.getInsuredCapital());
        this.setAdditionalPremiumPerPercentage(clfProductPlanCoverageDTO.getAdditionalPremiumPerPercentage());
        this.setAdditionalPremiumPerThousand(clfProductPlanCoverageDTO.getAdditionalPremiumPerThousand());
        this.setComment(clfProductPlanCoverageDTO.getComment());
        this.setCreatedAt(clfProductPlanCoverageDTO.getCreatedAt());
        this.setLastModifiedAt(clfProductPlanCoverageDTO.getLastModifiedAt());
        this.setRate(clfProductPlanCoverageDTO.getRate());
    }

    public CoveragePolicyItem(CoverageClf productPlan, long policyId, double requestAmount) {
        this.policyItemId = policyId;
        this.coverageProductPlanId = productPlan.getCoverageProductPlanId();
        this.insuredCapital = productPlan.getCoverageTypeIdc() != ClassifierEnum.COV_FIXED_CAPITAL.getReferenceCode() ?
                requestAmount :
                productPlan.getInsuredCapitalCoverage();
        this.additionalPremiumPerThousand = 0.0;
        this.additionalPremiumPerPercentage = 0.0;
        this.rate = productPlan.getRate();
    }

    //SMVS Constructors
    public CoveragePolicyItem(Long coveragePlanId, Long policyItemId, double totalInsuredCapital) {
        this.coverageProductPlanId = coveragePlanId;
        this.policyItemId = policyItemId;
        this.insuredCapital = totalInsuredCapital;
    }

    //VIN Constructor
    public CoveragePolicyItem(CoverageDTO coverageDTO, Long policyItemId) {
        this.setPolicyItemId(policyItemId);
        this.setCoverageProductPlanId(coverageDTO.getCoverageProductPlanId());
        this.setInsuredCapital(coverageDTO.getInsuredCapitalCoverage());
        this.setRate(coverageDTO.getRate());
    }

    //#endregion
}
