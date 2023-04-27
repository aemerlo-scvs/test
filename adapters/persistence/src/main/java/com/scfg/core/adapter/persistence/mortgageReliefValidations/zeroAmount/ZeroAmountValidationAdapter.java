package com.scfg.core.adapter.persistence.mortgageReliefValidations.zeroAmount;

import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementJpaEntity;
import com.scfg.core.application.port.out.mortgageReliefValidations.ZeroAmountValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static com.scfg.core.common.util.HelpersConstants.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class ZeroAmountValidationAdapter implements ZeroAmountValidationPort {

    @Override
    public ValidationResponseDTO zeroAmount(Double premiumAmount) {
        if (premiumAmount.doubleValue() == 0D) {
            return new ValidationResponseDTO(false, VR_PREMIUM_ZERO_AMOUNT);
        }
        return new ValidationResponseDTO(true, null);

    }

    public int calculateDisbursementDate(LocalDate disbursementDate, LocalDate currentDate) {
        if ((disbursementDate != null) && (currentDate != null)) {
            return Period.between(disbursementDate, currentDate).getMonths();
        } else {
            return -10;
        }
    }
}
