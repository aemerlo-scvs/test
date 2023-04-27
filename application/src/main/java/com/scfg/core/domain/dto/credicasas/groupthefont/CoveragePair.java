package com.scfg.core.domain.dto.credicasas.groupthefont;

import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.CoveragePolicyItem;
import com.scfg.core.domain.dto.credicasas.CoverageClf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class CoveragePair {
    private String coverageName;
    private Double insuredAmount;
    private Double additionalPremiumPercentage;
    private String entryAgeDescription;
    private String permanencyAgeDescription;
    private String commentAdditional;

    //#region Custom Constructor
    //GEL Constructors
    public CoveragePair(List<CoverageClf> coverageClfList,
                        CoveragePolicyItem coveragePolicyItem,
                        String operationNumber, Double insuredAmount) {
        CoverageClf coverageClf = coverageClfList.stream()
                .filter(x -> x.getCoverageProductPlanId().equals(coveragePolicyItem.getCoverageProductPlanId()))
                .findFirst().orElse(null);
        final String burial = "SEPELIO";

        if (coverageClf.getName().equals(burial)) {
            this.coverageName = coverageClf.getName();
            this.insuredAmount = coveragePolicyItem.getInsuredCapital();
            this.entryAgeDescription = coverageClf.getMinimumEntryAge() + " a " + coverageClf.getEntryAgeLimit() + " años cumplidos";
            this.permanencyAgeDescription = coverageClf.getAgeLimitStay() + " años cumplidos";
            this.commentAdditional = "USD 300,00 por asegurado";
        } else {
            this.coverageName = coverageClf.getName();
            this.insuredAmount = coveragePolicyItem.getInsuredCapital();
            this.entryAgeDescription = coverageClf.getMinimumEntryAge() + " a " + coverageClf.getEntryAgeLimit() + " años cumplidos";
            this.permanencyAgeDescription = coverageClf.getAgeLimitStay() + " años cumplidos";
            this.additionalPremiumPercentage = coveragePolicyItem.getAdditionalPremiumPerPercentage();
            this.commentAdditional = "Operación: " + operationNumber + " - " + "USD " + HelpersMethods.formatNumberWithThousandsSeparators(insuredAmount);
        }

    }

    //#endregion
}
