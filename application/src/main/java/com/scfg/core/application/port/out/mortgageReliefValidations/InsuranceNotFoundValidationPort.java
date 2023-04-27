package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

public interface InsuranceNotFoundValidationPort {

    ValidationResponseDTO insuranceNotFoundDHL(Long operationNumber, String ci, Long insurancePolicyHolder);
    ValidationResponseDTO insuranceNotFoundDHN(Long operationNumber, String ci, Long insurancePolicyHolder);
}
