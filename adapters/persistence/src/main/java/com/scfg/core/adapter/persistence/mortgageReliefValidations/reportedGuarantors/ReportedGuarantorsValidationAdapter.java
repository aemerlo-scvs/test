package com.scfg.core.adapter.persistence.mortgageReliefValidations.reportedGuarantors;

import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementJpaEntity;
import com.scfg.core.application.port.out.mortgageReliefValidations.ReportedGuarantorsValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.scfg.core.common.util.HelpersConstants.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class ReportedGuarantorsValidationAdapter implements ReportedGuarantorsValidationPort {


    @Override
    public ValidationResponseDTO reportedGuarantors(LocalDate disbursementDate, String borrowRole) {
        LocalDate date = LocalDate.parse(GUARANTOR_DATE_LIMIT);
        if (disbursementDate.isAfter(date) && borrowRole.equals(HelpersConstants.GUARANTOR)) {
            return new ValidationResponseDTO(false, VR_REPORTED_GUARANTORS_OUT_PERIOD_2019);
        }
        return new ValidationResponseDTO(true, null);
    }
}
