package com.scfg.core.adapter.persistence.mortgageReliefValidations.unusualCumulusIncrease;

import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementJpaEntity;
import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementRepository;
import com.scfg.core.application.port.out.mortgageReliefValidations.CumulusIncreaseValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import static com.scfg.core.common.util.HelpersConstants.*;
import static com.scfg.core.common.util.HelpersMethods.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class CumulusIncreaseValidationAdapter implements CumulusIncreaseValidationPort {

    private final MonthlyDisbursementRepository monthlyDisbursementRepository;

    @Override
    public ValidationResponseDTO cumulusIncreaseDHL(String clientCi, Integer numberMonth, Integer year) {
        ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO()
                .setCaseInOrder(true)
                .setExclusionDescription(null);

        double currentCumulus = monthlyDisbursementRepository.getCumulusByClientIdAndMonthIdAndYearId(clientCi, numberMonth, year).doubleValue();

        if (!existsNewDisbursementInCurrentMonth(clientCi, numberMonth, year)) {
            Map<String, Integer> prevPeriod = getPeriod(numberMonth, year,false);
            int prevMonthNumber = prevPeriod.get(KEY_MONTH), prevYear = prevPeriod.get(KEY_YEAR);

            double prevMonthCumulus = monthlyDisbursementRepository.getCumulusByClientIdAndMonthIdAndYearIdPastMonthlyDisbursement(clientCi, prevMonthNumber, prevYear);

            if (prevMonthCumulus < currentCumulus){
                validationResponseDTO.setCaseInOrder(false);
                validationResponseDTO.setExclusionDescription(VR_CUMULUS_INCREASE);
            }
        }
        return validationResponseDTO;

    }

    @Override
    public ValidationResponseDTO cumulusIncreaseDHN(String clientCi, Integer numberMonth, Integer year) {
        ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO()
                .setCaseInOrder(true)
                .setExclusionDescription(null);

        double currentCumulus = monthlyDisbursementRepository.getCumulusByClientIdAndMonthIdAndYearId(clientCi, numberMonth, year).doubleValue();

        if (!existsNewDisbursementInCurrentMonth(clientCi, numberMonth, year)) {
            Map<String, Integer> prevPeriod = getPeriod(numberMonth, year,false);
            int prevMonthNumber = prevPeriod.get(KEY_MONTH), prevYear = prevPeriod.get(KEY_YEAR);
            double prevMonthCumulus = monthlyDisbursementRepository.getCumulusByClientIdAndMonthIdAndYearIdPastMonthlyDisbursement(clientCi, prevMonthNumber, prevYear);

            if (prevMonthCumulus < currentCumulus){
                validationResponseDTO.setCaseInOrder(false);
                validationResponseDTO.setExclusionDescription(VR_CUMULUS_INCREASE);
            }
        }
        return validationResponseDTO;
    }

    private boolean existsNewDisbursementInCurrentMonth(String clientCi, int numberMonth, int year) {
        // Monthly Disbursment
        List<MonthlyDisbursementJpaEntity> monthlyDisbursements = monthlyDisbursementRepository
                .findMonthlyDisbursementByClientAndMonthAndYear(clientCi, numberMonth, year);

        return !monthlyDisbursements.isEmpty();

    }


}
