package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

import java.time.LocalDate;


public interface AgeOutOfRangeValidationPort {

    ValidationResponseDTO validateAgeOutOfRange(LocalDate birthDate, LocalDate disbursementDate);

}
