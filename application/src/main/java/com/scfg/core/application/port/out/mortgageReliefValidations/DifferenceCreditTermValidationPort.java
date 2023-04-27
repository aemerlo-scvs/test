package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

public interface DifferenceCreditTermValidationPort {

    ValidationResponseDTO validateCreditTermInRangeDHL(String clientCi, Long creditOperationNumber, Long insurancePolicyHolder);
    ValidationResponseDTO validateCreditTermInRangeDHN(String clientCi, Long creditOperationNumber, Long insurancePolicyHolder);
}
