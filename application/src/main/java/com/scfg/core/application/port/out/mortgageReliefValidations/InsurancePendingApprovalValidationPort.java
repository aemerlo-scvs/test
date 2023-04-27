package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

public interface InsurancePendingApprovalValidationPort {

    ValidationResponseDTO insurancePendingApprovalDHL(Long operationNumber, String ci, Long insurancePolicyHolder);
    ValidationResponseDTO insurancePendingApprovalDHN(Long operationNumber, String ci, Long insurancePolicyHolder);
}
