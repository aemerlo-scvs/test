package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

public interface DifferentExtraPremiumValidationPort {

    ValidationResponseDTO differentExtraPremiumDHL(Long operationNumber, String ci, Double extraPremiumRate, Long insurancePolicyHolder);
    ValidationResponseDTO differentExtraPremiumDHN(Long operationNumber, String ci, Double extraPremiumRate, Long insurancePolicyHolder,
                                                   String coverageType);
}
