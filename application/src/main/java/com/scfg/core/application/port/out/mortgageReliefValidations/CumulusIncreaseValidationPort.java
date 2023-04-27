package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

public interface CumulusIncreaseValidationPort {

    ValidationResponseDTO cumulusIncreaseDHL(String clientCi, Integer numberMonth, Integer year);
    ValidationResponseDTO cumulusIncreaseDHN(String clientCi, Integer numberMonth, Integer year);
}
