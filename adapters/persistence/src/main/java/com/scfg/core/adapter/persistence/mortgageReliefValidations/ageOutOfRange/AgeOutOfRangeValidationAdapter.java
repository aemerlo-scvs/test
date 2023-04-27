package com.scfg.core.adapter.persistence.mortgageReliefValidations.ageOutOfRange;

import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementJpaEntity;
import com.scfg.core.application.port.out.mortgageReliefValidations.AgeOutOfRangeValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static com.scfg.core.common.util.HelpersConstants.*;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.YEARS;

@PersistenceAdapter
@RequiredArgsConstructor
public class AgeOutOfRangeValidationAdapter implements AgeOutOfRangeValidationPort {

    @Override
    public ValidationResponseDTO validateAgeOutOfRange(LocalDate birthDate, LocalDate disbursementDate) {
        LocalDate currentDate = LocalDate.now();
        long age = calculateAge(birthDate, currentDate);
        long month = calculateDisbursementDate(disbursementDate, currentDate);
        if (month <= 1L && month != -10L) {
            if (age > HelpersConstants.ENTRY_AGE_LIMIT) {
                return new ValidationResponseDTO(false, VR_ENTRY_AGE_OUT_RANGE);
            } else {
                return new ValidationResponseDTO(true, null);
            }
        } else {
            if (age > HelpersConstants.STAY_AGE_LIMIT) {
                return new ValidationResponseDTO(false, VR_STAY_AGE_OUT_RANGE);
            } else {
                return new ValidationResponseDTO(true, null);
            }
        }
    }

    public long calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return YEARS.between(birthDate, currentDate);
        } else {
            return 0;
        }
    }

    public long calculateDisbursementDate(LocalDate disbursementDate, LocalDate currentDate) {
        if ((disbursementDate != null) && (currentDate != null)) {
            return MONTHS.between(disbursementDate, currentDate);
        } else {
            return -10L;
        }
    }
}
