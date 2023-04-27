package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ProductCalculationsUseCase;
import com.scfg.core.application.port.out.ProductPort;
import com.scfg.core.common.enums.ProductEnum;
import com.scfg.core.domain.PolicyItem;
import com.scfg.core.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCalculationsService implements ProductCalculationsUseCase {

    private final ProductPort productPort;
    @Override
    public PolicyItem calculateVariables(PolicyItem policyItem) {
        Product product = productPort.findProductByPolicyId(policyItem.getPolicyId());
        if (product.getInitials().equals(ProductEnum.DHN.getValue())) {
            calculateDHN(policyItem);
        }
        if (product.getInitials().equals(ProductEnum.SMVS.getValue())) {
            calculateSMVS(policyItem);
        }
        if (product.getInitials().equals(ProductEnum.VIN.getValue())) {
            calculateVIN(policyItem);
        }
        return policyItem;
    }

    @Override
    public PolicyItem calculateVariablesWithYear(PolicyItem policyItem, Integer year, Double intermediaryCommissionRate) {
        Product product = productPort.findProductByPolicyId(policyItem.getPolicyId());
        if (product.getInitials().equals(ProductEnum.VIN.getValue())) {
            calculateVIN(policyItem,year,intermediaryCommissionRate);
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

    private void calculateVIN(PolicyItem policyItem) {
        Integer individualPremiumAux = 600;
        policyItem.setIndividualNetPremium((policyItem.getIndividualPremium() * 0.49));
        policyItem.setIndividualAdditionalPremium(policyItem.getIndividualPremium() - policyItem.getIndividualNetPremium());
        policyItem.setIndividualRiskPremium(190.05 * Math.floor(policyItem.getIndividualPremium() / individualPremiumAux));
        policyItem.setAPS(policyItem.getIndividualNetPremium() * 0.01);
        policyItem.setFPA(policyItem.getIndividualNetPremium() * 0.0025);
        policyItem.setIndividualCollectionServiceCommission(policyItem.getIndividualPremium() * 0.50);
    }

    private void calculateVIN(PolicyItem policyItem, int year, Double intermediaryCommissionRate) {
        policyItem.setIndividualNetPremium((policyItem.getIndividualPremium() * 0.49));
        policyItem.setIndividualAdditionalPremium(policyItem.getIndividualPremium() - policyItem.getIndividualNetPremium());
        policyItem.setIndividualRiskPremium(251.65 * year);
        policyItem.setAPS(policyItem.getIndividualNetPremium() * 0.01);
        policyItem.setFPA(policyItem.getIndividualNetPremium() * 0.0025);
        policyItem.setIndividualCollectionServiceCommission(policyItem.getIndividualPremium() * 0.50);
        policyItem.setIndividualIntermediaryCommission(intermediaryCommissionRate * policyItem.getIndividualPremium());
    }
}
