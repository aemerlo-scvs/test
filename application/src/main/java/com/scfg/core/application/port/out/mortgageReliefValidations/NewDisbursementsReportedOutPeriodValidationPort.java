package com.scfg.core.application.port.out.mortgageReliefValidations;

import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;

import java.util.List;

public interface NewDisbursementsReportedOutPeriodValidationPort {

    ValidationResponseDTO validateDisbursementsReportedInPeriodAllowedDHL(Integer monthNumber, Integer year,
                                                                          List<MonthlyDisbursementDhlDTO> monthlyDisbursementList,
                                                                          List<MonthlyDisbursementDhlDTO> pastMonthlyDisbursementList);
    ValidationResponseDTO validateDisbursementsReportedInPeriodAllowedDHN(Integer monthNumber, Integer year,
                                                                          List<MonthlyDisbursementDhnDTO> monthlyDisbursementList,
                                                                          List<MonthlyDisbursementDhnDTO> pastMonthlyDisbursementList);
}
