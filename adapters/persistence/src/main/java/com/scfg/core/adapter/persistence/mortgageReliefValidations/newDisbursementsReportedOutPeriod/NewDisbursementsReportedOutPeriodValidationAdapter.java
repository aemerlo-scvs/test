package com.scfg.core.adapter.persistence.mortgageReliefValidations.newDisbursementsReportedOutPeriod;

import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementRepository;
import com.scfg.core.application.port.out.mortgageReliefValidations.NewDisbursementsReportedOutPeriodValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.scfg.core.common.util.HelpersConstants.*;


@PersistenceAdapter
@RequiredArgsConstructor
public class NewDisbursementsReportedOutPeriodValidationAdapter implements NewDisbursementsReportedOutPeriodValidationPort {

    private final MonthlyDisbursementRepository monthlyDisbursementRepository;

    @Override
    public ValidationResponseDTO validateDisbursementsReportedInPeriodAllowedDHL(Integer monthNumber, Integer year,
                                                                                 List<MonthlyDisbursementDhlDTO> monthlyDisbursementList,
                                                                                 List<MonthlyDisbursementDhlDTO> pastMonthlyDisbursementList) {
        ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO()
                .setCaseInOrder(true)
                .setExclusionDescription(null);

        // Get Monthly disbursements of different period
        List<MonthlyDisbursementDhlDTO> disbursementsDifferentAtCurrentMonth = monthlyDisbursementList.stream()
                .filter(monthlyDisbursementDhlDTO -> monthlyDisbursementDhlDTO.getFECHA_DESEMBOLSO().getMonthValue() != monthNumber
                        || (monthlyDisbursementDhlDTO.getFECHA_DESEMBOLSO().getMonthValue() == monthNumber
                        && monthlyDisbursementDhlDTO.getFECHA_DESEMBOLSO().getYear() != year))
                .collect(Collectors.toList());

        // Get Past Monthly Disbursements
        List<MonthlyDisbursementDhlDTO> disbursementsReportedOutPeriod = disbursementsDifferentAtCurrentMonth.stream()
                .filter(currentMonthlyDisbursement -> pastMonthlyDisbursementList.stream()
                        .map(MonthlyDisbursementDhlDTO::getNRO_OPERACION).noneMatch(operation -> operation
                                .equals(currentMonthlyDisbursement.getNRO_OPERACION())))
                .collect(Collectors.toList());

        if (!disbursementsReportedOutPeriod.isEmpty() && !disbursementsDifferentAtCurrentMonth.isEmpty()) {
            validationResponseDTO.setCaseInOrder(false)
                    .setExclusionDescription(VR_REPORTED_NEW_DISBURSEMENT_OUT_PERIOD)
                    .setData(disbursementsReportedOutPeriod);
        }
        return validationResponseDTO;

    }

    @Override
    public ValidationResponseDTO validateDisbursementsReportedInPeriodAllowedDHN(Integer monthNumber, Integer year,
                                                                                 List<MonthlyDisbursementDhnDTO> monthlyDisbursementList,
                                                                                 List<MonthlyDisbursementDhnDTO> pastMonthlyDisbursementList) {

        ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO()
                .setCaseInOrder(true)
                .setExclusionDescription(null);

        // Get Monthly disbursements of different period
        List<MonthlyDisbursementDhnDTO> disbursementsDifferentAtCurrentMonth = monthlyDisbursementList.stream()
                .filter(monthlyDisbursementDhlDTO -> monthlyDisbursementDhlDTO.getFECHA_DESEMBOLSO().getMonthValue() != monthNumber
                        || (monthlyDisbursementDhlDTO.getFECHA_DESEMBOLSO().getMonthValue() == monthNumber
                        && monthlyDisbursementDhlDTO.getFECHA_DESEMBOLSO().getYear() != year))
                .collect(Collectors.toList());

        // Get Past Monthly Disbursements
        List<MonthlyDisbursementDhnDTO> disbursementsReportedOutPeriod = disbursementsDifferentAtCurrentMonth.stream()
                .filter(currentMonthlyDisbursement -> pastMonthlyDisbursementList.stream()
                        .map(MonthlyDisbursementDhnDTO::getNRO_OPERACION).noneMatch(operation -> operation
                                .equals(currentMonthlyDisbursement.getNRO_OPERACION())))
                .collect(Collectors.toList());

        if (!disbursementsReportedOutPeriod.isEmpty() && !disbursementsDifferentAtCurrentMonth.isEmpty()) {
            validationResponseDTO.setCaseInOrder(false)
                    .setExclusionDescription(VR_REPORTED_NEW_DISBURSEMENT_OUT_PERIOD)
                    .setData(disbursementsReportedOutPeriod);
        }
        return validationResponseDTO;
    }

}
