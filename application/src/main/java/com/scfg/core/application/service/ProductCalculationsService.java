package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ProductCalculationsUseCase;
import com.scfg.core.application.port.out.ProductPort;
import com.scfg.core.common.enums.PolicyMovementTypeClassifierEnum;
import com.scfg.core.common.enums.ProductEnum;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.domain.PolicyItem;
import com.scfg.core.domain.Product;
import com.scfg.core.domain.common.PolicyItemEconomic;
import com.scfg.core.domain.common.PolicyItemEconomicReinsurance;
import com.scfg.core.domain.dto.CoverageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductCalculationsService implements ProductCalculationsUseCase {

    private final ProductPort productPort;


    //#region PolicyItem Calcs

    @Override
    public PolicyItem calculateVariables(PolicyItem policyItem) {
        Product product = productPort.findProductByPolicyId(policyItem.getPolicyId());
        if (product.getInitials().equals(ProductEnum.DHN.getValue())) {
            calculateDHN(policyItem);
        }
        if (product.getInitials().equals(ProductEnum.SMVS.getValue())) {
            calculateSMVS(policyItem);
        }
        return policyItem;
    }

    private void calculateDHN(PolicyItem policyItem) {
        policyItem.setIndividualPremium(policyItem.getIndividualInsuredCapital() * policyItem.getIndividualPremiumRate());
        policyItem.setIndividualNetPremium(policyItem.getIndividualPremium() * 0.95);
        policyItem.setIndividualAdditionalPremium(policyItem.getIndividualPremium() * 0.05);
        policyItem.setAPS(policyItem.getIndividualNetPremium() * 0.01);
        policyItem.setFPA(policyItem.getIndividualNetPremium() * 0.025);
        policyItem.setIndividualIntermediaryCommission(policyItem.getIndividualNetPremium() * 0.08);
    }

    private void calculateSMVS(PolicyItem policyItem) {
        policyItem.setIndividualNetPremium(policyItem.getIndividualPremium());
        policyItem.setAPS(policyItem.getIndividualNetPremium() * 0.01);
        policyItem.setFPA(policyItem.getIndividualNetPremium() * 0.025);
        policyItem.setIndividualCollectionServiceCommission(policyItem.getIndividualPremium() * 0.3);
    }

    //#endregion

    //#region PolicyItemEconomic Calcs

    public void calcPolicyItemEconomicVIN(PolicyItemEconomic actualPolicyItemEconomic,
                                          PolicyItemEconomic oldPolicyItemEconomic,
                                          double totalPremiumCeded,
                                          int yearsOfValidity, Double intermediaryCommissionRate,
                                          Date policyFromDate, Date policyToDate, Date requestDate) {
        int daysPassed = DateUtils.daysBetween(policyFromDate, requestDate).intValue();
        double factor = (actualPolicyItemEconomic.getMovementTypeIdc().equals(PolicyMovementTypeClassifierEnum.PRODUCTION.getValue())) ? 1 : -1;
        double auxIndividualPremium = actualPolicyItemEconomic.getIndividualPremium();

        actualPolicyItemEconomic.setIndividualIntermediaryCommissionPercentage(intermediaryCommissionRate * 100);
        actualPolicyItemEconomic.setExchangeRate(1.0);

        if (actualPolicyItemEconomic.getIndividualPremium() <= 0) {
            double defaultValue = 0.0;
            actualPolicyItemEconomic.setIndividualNetPremium(defaultValue);
            actualPolicyItemEconomic.setIndividualAdditionalPremium(defaultValue);
            actualPolicyItemEconomic.setIndividualRiskPremium(defaultValue);
            actualPolicyItemEconomic.setAPS(defaultValue);
            actualPolicyItemEconomic.setFPA(defaultValue);
            actualPolicyItemEconomic.setIndividualIntermediaryCommission(defaultValue);
            actualPolicyItemEconomic.setIndividualPremiumCeded(defaultValue);
            actualPolicyItemEconomic.setIndividualCollectionServiceCommission(defaultValue);
        }

        if (oldPolicyItemEconomic != null &&
                actualPolicyItemEconomic.getIndividualPremium() > oldPolicyItemEconomic.getIndividualPremium()) {
            actualPolicyItemEconomic.setIndividualPremium(oldPolicyItemEconomic.getIndividualPremium());
        }

        if (actualPolicyItemEconomic.getIndividualPremium() > 0) {
            if (daysPassed <= 30) {
                actualPolicyItemEconomic.setIndividualNetPremium(actualPolicyItemEconomic.getIndividualPremium() * 0.49);

                actualPolicyItemEconomic.setIndividualAdditionalPremium(actualPolicyItemEconomic.getIndividualPremium() -
                        actualPolicyItemEconomic.getIndividualNetPremium());

                actualPolicyItemEconomic.setIndividualRiskPremium(251.65 * yearsOfValidity);

                actualPolicyItemEconomic.setAPS(actualPolicyItemEconomic.getIndividualNetPremium() * 0.01);

                actualPolicyItemEconomic.setFPA(actualPolicyItemEconomic.getIndividualNetPremium() * 0.0025);

                actualPolicyItemEconomic.setIndividualIntermediaryCommission(intermediaryCommissionRate *
                        actualPolicyItemEconomic.getIndividualNetPremium());

                actualPolicyItemEconomic.setIndividualPremiumCeded(totalPremiumCeded);

                actualPolicyItemEconomic.setIndividualCollectionServiceCommission(actualPolicyItemEconomic.getIndividualPremium() * 0.50);

            } else {
                int daysOfPolicyValidity = DateUtils.daysBetween(policyFromDate, policyToDate).intValue();

                double individualNetPremium = (actualPolicyItemEconomic.getIndividualPremium() * oldPolicyItemEconomic.getIndividualNetPremium()) /
                        oldPolicyItemEconomic.getIndividualPremium();
                actualPolicyItemEconomic.setIndividualNetPremium(individualNetPremium);

                double individualAdditionalPremium = (actualPolicyItemEconomic.getIndividualPremium() *
                        oldPolicyItemEconomic.getIndividualAdditionalPremium()) /
                        oldPolicyItemEconomic.getIndividualPremium();
                actualPolicyItemEconomic.setIndividualAdditionalPremium(individualAdditionalPremium);

                double individualRiskPremium = getProRateCalc(oldPolicyItemEconomic.getIndividualRiskPremium(), daysPassed,
                        daysOfPolicyValidity);
                actualPolicyItemEconomic.setIndividualRiskPremium(individualRiskPremium);

                actualPolicyItemEconomic.setAPS(actualPolicyItemEconomic.getIndividualNetPremium() * 0.01);

                actualPolicyItemEconomic.setFPA(actualPolicyItemEconomic.getIndividualNetPremium() * 0.0025);

                actualPolicyItemEconomic.setIndividualIntermediaryCommission(intermediaryCommissionRate *
                        actualPolicyItemEconomic.getIndividualNetPremium());

                actualPolicyItemEconomic.setIndividualPremiumCeded(totalPremiumCeded);

                double individualCollectionServiceCommission = getProRateCalc(oldPolicyItemEconomic.getIndividualCollectionServiceCommission(),
                        daysPassed, daysOfPolicyValidity);
                actualPolicyItemEconomic.setIndividualCollectionServiceCommission(individualCollectionServiceCommission);
            }
        }

        if (factor <= 0) {
            multiplyWithFactor(actualPolicyItemEconomic, factor);
        }

    }

    public void multiplyWithFactor(PolicyItemEconomic policyItemEconomic, double factor) {
        policyItemEconomic.setIndividualPremium(policyItemEconomic.getIndividualPremium() * factor);

        policyItemEconomic.setIndividualNetPremium(policyItemEconomic.getIndividualNetPremium() * factor);

        policyItemEconomic.setIndividualAdditionalPremium(policyItemEconomic.getIndividualAdditionalPremium() * factor);

        policyItemEconomic.setIndividualRiskPremium(policyItemEconomic.getIndividualRiskPremium() * factor);

        policyItemEconomic.setAPS(policyItemEconomic.getAPS() * factor);

        policyItemEconomic.setFPA(policyItemEconomic.getFPA() * factor);

        policyItemEconomic.setIndividualIntermediaryCommission(policyItemEconomic.getIndividualIntermediaryCommission() * factor);

        policyItemEconomic.setIndividualPremiumCeded(policyItemEconomic.getIndividualPremiumCeded() * factor);

        policyItemEconomic.setIndividualCollectionServiceCommission(policyItemEconomic.getIndividualCollectionServiceCommission() * factor);
    }

    //#endregion

    //#region PolicyItemEconomicReinsurance Calcs

    public void calcPolicyItemEconomicReinsuranceVIN(PolicyItemEconomicReinsurance actualPolicyItemEconomicReinsurance,
                                                     List<PolicyItemEconomicReinsurance> oldPolicyItemEconomicReinsuranceList,
                                                     int movementTypeIdc,
                                                     CoverageDTO coverage, Double policyNetPremium,
                                                     int yearsOfValidity, Date policyFromDate, Date policyToDate, Date requestDate) {
        int daysPassed = DateUtils.daysBetween(policyFromDate, requestDate).intValue();
        double factor = (movementTypeIdc == PolicyMovementTypeClassifierEnum.PRODUCTION.getValue()) ? 1 : -1;
        String coverageNameAux = coverage.getCoverageName().toUpperCase().trim();
        double premiumCeded = 0.0;

        if (Math.abs(policyNetPremium) <= 0) {
            double defaultValue = 0.0;
            actualPolicyItemEconomicReinsurance.setPremiumCeded(defaultValue);
        }

        if (Math.abs(policyNetPremium) > 0) {
            if (daysPassed <= 30) {

                if (coverageNameAux.contains("MUERTE")) {
                    premiumCeded = 90.3 * yearsOfValidity;
                }
                if (coverageNameAux.contains("INDEMNIZACIÓN ADICIONAL")) {
                    premiumCeded = 17.5 * yearsOfValidity;
                }
                if (coverageNameAux.contains("GASTOS MÉDICOS")) {
                    premiumCeded = 18.02 * yearsOfValidity;
                }
                actualPolicyItemEconomicReinsurance.setPremiumCeded(premiumCeded * yearsOfValidity);
            } else {
                int daysOfPolicyValidity = DateUtils.daysBetween(policyFromDate, policyToDate).intValue();

                PolicyItemEconomicReinsurance oldPolicyItemEconomicReinsurance = oldPolicyItemEconomicReinsuranceList.stream()
                        .filter(e -> Objects.equals(e.getCoverageId(), coverage.getCoverageId()))
                        .findFirst()
                        .orElse(null);

                if (oldPolicyItemEconomicReinsurance == null) {
                    throw new OperationException("Valor de la prima cedida anterior no encontrado al momento de calcular el valor de la prima cedida para la anulación");
                }

                premiumCeded = getProRateCalc(oldPolicyItemEconomicReinsurance.getPremiumCeded(), daysPassed, daysOfPolicyValidity);
                actualPolicyItemEconomicReinsurance.setPremiumCeded(premiumCeded);
            }
        }

        if (factor <= 0) {
            actualPolicyItemEconomicReinsurance.setPremiumCeded(actualPolicyItemEconomicReinsurance.getPremiumCeded() * factor);
        }

    }

    public double calcPolicyItemEconomicReinsuranceTotalPremiumCededVIN(List<PolicyItemEconomicReinsurance> oldPolicyItemEconomicReinsuranceList,
                                                                      int yearsOfValidity, Date policyFromDate,
                                                                      Date policyToDate, Date requestDate) {
        int daysPassed = DateUtils.daysBetween(policyFromDate, requestDate).intValue();
        if (daysPassed <= 30) {
            return (90.3 * yearsOfValidity) + (17.5 * yearsOfValidity) + (18.02 * yearsOfValidity);
        } else {
            double totalPremiumCeded = 0.0;
            int daysOfPolicyValidity = DateUtils.daysBetween(policyFromDate, policyToDate).intValue();

            for (int i = 0; i < oldPolicyItemEconomicReinsuranceList.size(); i++) {
                PolicyItemEconomicReinsurance o = oldPolicyItemEconomicReinsuranceList.get(i);
                double premiumCeded = getProRateCalc(o.getPremiumCeded(), daysPassed, daysOfPolicyValidity);
                totalPremiumCeded += premiumCeded;
            }
            return totalPremiumCeded;
        }
    }

    private double getProRateCalc(double value, int daysPassed, int daysOfPolicyValidity) {
        return value - (daysPassed * value / (daysOfPolicyValidity));
    }

    //#endregion

}
