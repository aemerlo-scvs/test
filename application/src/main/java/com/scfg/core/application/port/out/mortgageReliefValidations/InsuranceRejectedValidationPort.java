package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

public interface InsuranceRejectedValidationPort {

    ValidationResponseDTO insuranceRejectedDHL(Long operationNumber, String ci, Long insurancePolicyHolder);
    ValidationResponseDTO insuranceRejectedDHN(Long operationNumber, String ci, Long insurancePolicyHolder);
}
