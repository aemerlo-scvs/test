package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

public interface DuplicateOperationsValidationPort {

    ValidationResponseDTO validateDuplicateOperationsDHL(Long operationNumber, String ci, Long insurancePolicyHolder);
    ValidationResponseDTO validateDuplicateOperationsDHN(Long operationNumber, String ci, Long insurancePolicyHolder);
}
