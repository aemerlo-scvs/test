package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

import java.util.List;

public interface LimitCoverageCreditLineValidationPort {

    ValidationResponseDTO validateLimitCoverageForCreditLineDHN(String clientCi, Long creditOperationNumber,
                                                                List<MonthlyDisbursementDhnDTO> monthlyDisbursementDhnList);
}
