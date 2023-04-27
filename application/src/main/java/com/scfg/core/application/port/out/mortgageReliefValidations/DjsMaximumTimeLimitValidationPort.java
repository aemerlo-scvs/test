package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

import java.time.LocalDate;

public interface DjsMaximumTimeLimitValidationPort {

    ValidationResponseDTO djsMaximumTimeLimitDHL(Long operationNumber, String documentNumber, LocalDate disbursementDate, Long insurancePolicyHolder);
    ValidationResponseDTO djsMaximumTimeLimitDHN(Long operationNumber, String documentNumber, LocalDate disbursementDate, Long insurancePolicyHolder);
}
