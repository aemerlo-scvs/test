package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

public interface ZeroAmountValidationPort {

    ValidationResponseDTO zeroAmount(Double premiumAmount);
}
