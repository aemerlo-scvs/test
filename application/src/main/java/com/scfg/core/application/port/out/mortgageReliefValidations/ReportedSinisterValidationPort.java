package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

public interface ReportedSinisterValidationPort {

    ValidationResponseDTO reportedSinisterDHL(String documentNumber, Long insurancePolicyHolder);
    ValidationResponseDTO reportedSinisterDHN(String documentNumber, Long insurancePolicyHolder);
}
