package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

public interface DifferenceAmountDisbursementValidationPort {

    ValidationResponseDTO validateEqualityInAmountsDisbursementAndSubscribedDHL(Long creditOperationNumber, String clientCi, Long insurancePolicyHolder);
    ValidationResponseDTO validateEqualityInAmountsDisbursementAndSubscribedDHN(Long creditOperationNumber, String clientCi, Long insurancePolicyHolder);
}
